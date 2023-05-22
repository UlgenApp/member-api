package tr.edu.ku.ulgen.service;

import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tr.edu.ku.ulgen.dto.AuthenticationDto;
import tr.edu.ku.ulgen.dto.RegisterDto;
import tr.edu.ku.ulgen.entity.Role;
import tr.edu.ku.ulgen.entity.Token;
import tr.edu.ku.ulgen.entity.TokenType;
import tr.edu.ku.ulgen.entity.User;
import tr.edu.ku.ulgen.repository.TokenRepository;
import tr.edu.ku.ulgen.repository.UserRepository;
import tr.edu.ku.ulgen.response.*;

import java.util.*;

/**
 * A service class responsible for managing user authentication and registration.
 * It provides methods to register new users, authenticate existing users, and manage user tokens.
 *
 * @author Kaan Turkmen
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailSenderService emailSenderService;

    /**
     * Registers a new user with the provided registration information.
     *
     * @param registerDto the registration data transfer object containing user registration information.
     * @return a {@link ResponseEntity} containing {@link RegisterResponse} with the result of the operation.
     */
    public ResponseEntity<RegisterResponse> register(RegisterDto registerDto) {
        boolean mailExists;

        try {
            mailExists = userRepository.existsByEmail(registerDto.getEmail());
        } catch (PersistenceException e) {
            log.error("Could not called existByEmail on the database.");
            log.error("Database is not reachable, {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(RegisterResponse.builder().result("SERVICE_UNAVAILABLE").build());
        }

        if (mailExists) {
            log.error("Mail could not found in database for: {}", registerDto.getEmail());
            return ResponseEntity.badRequest().body(RegisterResponse.builder().result("EMAIL_IS_TAKEN").build());
        }

        User user = User.builder().firstName(registerDto.getFirstName()).lastName(registerDto.getLastName()).email(registerDto.getEmail()).password(passwordEncoder.encode(registerDto.getPassword())).role(Role.USER).additionalInfo(registerDto.getAdditionalInfo()).build();

        log.info("Saving user to the database.");

        User savedUser;

        try {
            savedUser = this.userRepository.save(user);
        } catch (PersistenceException e) {
            log.error("Could not save user on the database.");
            log.error("Database is not reachable, {}", e.getMessage());
            return null;
        }

        log.info("Generating token.");

        String jwtToken = jwtService.generateToken(Map.of("email_verification", true), user);

        log.info("Saving token to the database.");
        saveUserToken(savedUser, jwtToken);

        String emailBody = "Merhaba Ülgenli,\n\n" +
                "Uygulamamızı kullanabilmen için sadece bir adım kaldı. Uygulamamızın güvenliğini sağlaman için e-mail adresini doğrulaman gerekiyor. Bunun için aşağıdaki bağlantıya tıklaman yeterli, şimdiden iyi eğlenceler!\n\n" +
                "https://ulgen.app/verify-email?token=" + jwtToken + "\n\n" +
                "Kendine iyi bak,\n" +
                "Ülgen.";

        emailSenderService.sendEmail(savedUser.getEmail(), "Ülgen Hesabını Onayla", emailBody);

        return ResponseEntity.ok().body(RegisterResponse.builder().result("SUCCESS").build());
    }

    /**
     * Authenticates an existing user with the provided authentication information.
     *
     * @param authenticationDto the authentication data transfer object containing user authentication information.
     * @return a {@link ResponseEntity} containing {@link AuthenticationResponse} with the result of the operation.
     */
    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationDto authenticationDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationDto.getEmail(), authenticationDto.getPassword()));

        log.info("Trying to find user with email.");

        User user;

        try {
            user = userRepository.findByEmail(authenticationDto.getEmail()).orElseThrow();
        } catch (NoSuchElementException nse) {
            log.error("User with this email does not exist in the database.");
            return ResponseEntity.badRequest().body(AuthenticationResponse.builder().result("USER_NOT_FOUND").build());
        } catch (PersistenceException e) {
            log.error("Could not called findByEmail on the database.");
            log.error("Database is not reachable, {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(AuthenticationResponse.builder().result("SERVICE_UNAVAILABLE").build());
        }

        log.info("Generating token.");
        String jwtToken = jwtService.generateToken(user);

        log.info("Revoking other tokens.");
        revokeAllUserTokens(user);

        log.info("Saving tokens to the database.");
        saveUserToken(user, jwtToken);

        return ResponseEntity.ok().body(AuthenticationResponse.builder().result("SUCCESS").token(jwtToken).build());
    }

    /**
     * Verifies the email of a user using a provided JWT token.
     *
     * @param token the JWT token to be used for email verification.
     * @return a {@link ResponseEntity} containing a {@link VerifyEmailResponse} with the result of the operation.
     */
    public ResponseEntity<VerifyEmailResponse> verifyEmail(String token) {
        boolean emailVerification = jwtService.hasClaim(token, "email_verification");

        if (!emailVerification) {
            return ResponseEntity.badRequest().body(VerifyEmailResponse.builder().result("BAD_TOKEN").build());
        }

        String email = jwtService.extractUsername(token);
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(VerifyEmailResponse.builder().result("USER_NOT_FOUND").build());
        }

        if (jwtService.isTokenExpired(token)) {
            return ResponseEntity.badRequest().body(VerifyEmailResponse.builder().result("TOKEN_EXPIRED").build());
        }

        User currentUser = user.get();
        currentUser.setEnabled(true);

        try {
            userRepository.save(currentUser);
        } catch (PersistenceException e) {
            log.error("Could not update user's enabled property on the database.");
            log.error("Database is not reachable, {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(VerifyEmailResponse.builder().result("SERVICE_UNAVAILABLE").build());
        }

        revokeAllUserTokens(currentUser);

        return ResponseEntity.ok().body(VerifyEmailResponse.builder().result("SUCCESS").build());
    }

    /**
     * Resends the email verification link to the user with the provided email.
     *
     * @param email the email address of the user to resend the email verification link to.
     * @return a {@link ResponseEntity} containing a {@link VerifyEmailResponse} with the result of the operation.
     */
    public ResponseEntity<VerifyEmailResponse> resendEmailVerification(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(VerifyEmailResponse.builder().result("USER_NOT_FOUND").build());
        }

        User currentUser = user.get();

        if (currentUser.isEnabled()) {
            return ResponseEntity.badRequest().body(VerifyEmailResponse.builder().result("USER_ALREADY_VERIFIED").build());
        }

        revokeAllUserTokens(currentUser);

        String jwtToken = jwtService.generateToken(Map.of("email_verification", true), currentUser);

        Token token = Token.builder().user(currentUser).token(jwtToken).build();

        try {
            tokenRepository.save(token);
        } catch (PersistenceException e) {
            log.error("Could not save token on the database.");
            log.error("Database is not reachable, {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(VerifyEmailResponse.builder().result("SERVICE_UNAVAILABLE").build());
        }

        String emailBody = "Merhaba Ülgenli,\n\n" +
                "Uygulamamızı kullanabilmen için sadece bir adım kaldı. Uygulamamızın güvenliğini sağlaman için e-mail adresini doğrulaman gerekiyor. Bunun için aşağıdaki bağlantıya tıklaman yeterli, şimdiden iyi eğlenceler!\n\n" +
                "https://ulgen.app/verify-email?token=" + jwtToken + "\n\n" +
                "Kendine iyi bak,\n" +
                "Ülgen.";

        emailSenderService.sendEmail(currentUser.getEmail(), "Ülgen Hesabını Onayla", emailBody);

        return ResponseEntity.ok().body(VerifyEmailResponse.builder().result("SUCCESS").build());
    }

    /**
     * Generates a password reset link for the user with the provided email and sends it via email.
     *
     * @param email the email address of the user who wants to reset their password.
     * @return a {@link ResponseEntity} containing a {@link ForgotPasswordResponse} with the result of the operation.
     */
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(ForgotPasswordResponse.builder().result("USER_NOT_FOUND").build());
        }

        User u = user.get();

        revokeAllUserTokens(u);

        String token = jwtService.generateToken(Map.of("password_reset", true), u);
        Token t = Token.builder().token(token).tokenType(TokenType.BEARER).expired(false).revoked(false).user(u).build();

        try {
            tokenRepository.save(t);
        } catch (PersistenceException e) {
            log.error("Could not save password reset token on the database.");
            log.error("Database is not reachable, {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ForgotPasswordResponse.builder().result("SERVICE_UNAVAILABLE").build());
        }

        String emailBody = "Merhaba Ülgenli,\n\n" +
                "Bizden kısa bir süre önce bir şifre sıfırlama isteğinde bulundun. Aşağıdaki bağlantıda yer alan adımları takip ederek bunu gerçekleştirebilirsin. Eğer ki bu istekte bulunan kişi sen değilsen şifreni değiştirmeni tavsiye ediyoruz.\n\n" +
                "https://ulgen.app/reset-password?token=" + token + "\n\n" +
                "Kendine iyi bak,\n" +
                "Ülgen.";

        emailSenderService.sendEmail(u.getEmail(), "Ülgen Şifre Sıfırlama Bağlantın", emailBody);

        return ResponseEntity.ok().body(ForgotPasswordResponse.builder().result("SUCCESS").build());
    }

    /**
     * Resets the password of a user using a provided token and a new password.
     *
     * @param token       the token to be used for resetting the password.
     * @param newPassword the new password to be set for the user.
     * @return a {@link ResponseEntity} containing a {@link ResetPasswordResponse} with the result of the operation.
     */
    public ResponseEntity<ResetPasswordResponse> resetPassword(String token, String newPassword) {
        boolean passwordReset = jwtService.hasClaim(token, "password_reset");
        Optional<Token> t;

        try {
            t = tokenRepository.findByToken(token);
        } catch (PersistenceException e) {
            log.error("Could not execute find by token on the database.");
            log.error("Database is not reachable, {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ResetPasswordResponse.builder().result("SERVICE_UNAVAILABLE").build());
        }

        if (t.isEmpty()) {
            return ResponseEntity.badRequest().body(ResetPasswordResponse.builder().result("BAD_TOKEN").build());
        }

        if (t.get().isRevoked()) {
            return ResponseEntity.badRequest().body(ResetPasswordResponse.builder().result("USED_TOKEN").build());
        }

        if (!passwordReset) {
            return ResponseEntity.badRequest().body(ResetPasswordResponse.builder().result("BAD_TOKEN").build());
        }

        if (jwtService.isTokenExpired(token)) {
            return ResponseEntity.badRequest().body(ResetPasswordResponse.builder().result("TOKEN_EXPIRED").build());
        }

        String email = jwtService.extractUsername(token);

        Optional<User> user;

        try {
            user = userRepository.findByEmail(email);
        } catch (PersistenceException e) {
            log.error("Could not execute find by email on the database.");
            log.error("Database is not reachable, {}", e.getMessage());

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ResetPasswordResponse.builder().result("SERVICE_UNAVAILABLE").build());
        }

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(ResetPasswordResponse.builder().result("USER_NOT_FOUND").build());
        }

        User currentUser = user.get();
        currentUser.setPassword(passwordEncoder.encode(newPassword));

        try {
            userRepository.save(currentUser);
        } catch (PersistenceException e) {
            log.error("Could not update user's enabled property on the database.");
            log.error("Database is not reachable, {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ResetPasswordResponse.builder().result("SERVICE_UNAVAILABLE").build());
        }

        revokeAllUserTokens(currentUser);

        return ResponseEntity.ok().body(ResetPasswordResponse.builder().result("SUCCESS").build());
    }

    /**
     * Saves a JWT token for the given user.
     *
     * @param user     the user entity for which the JWT token should be saved.
     * @param jwtToken the JWT token to be saved.
     */
    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder().user(user).token(jwtToken).tokenType(TokenType.BEARER).expired(false).revoked(false).build();

        try {
            tokenRepository.save(token);
        } catch (PersistenceException e) {
            log.error("Could not save token on the database.");
            log.error("Database is not reachable, {}", e.getMessage());
        }
    }

    /**
     * Revokes all valid JWT tokens for the given user.
     *
     * @param user the user entity for which all valid JWT tokens should be revoked.
     */
    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = new ArrayList<>();

        try {
            validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        } catch (PersistenceException e) {
            log.error("Could not called findAllValidTokenByUsers on the database.");
            log.error("Database is not reachable, {}", e.getMessage());
        }

        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        try {
            tokenRepository.saveAll(validUserTokens);
        } catch (PersistenceException e) {
            log.error("Could not save all tokens on the database.");
            log.error("Database is not reachable, {}", e.getMessage());
        }
    }
}
