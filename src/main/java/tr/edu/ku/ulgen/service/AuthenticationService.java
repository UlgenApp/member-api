package tr.edu.ku.ulgen.service;

import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import tr.edu.ku.ulgen.response.AuthenticationResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

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

    /**
     * Registers a new user with the provided registration information.
     *
     * @param registerDto the registration data transfer object containing user registration information.
     * @return an authentication response containing a JWT token, or null if registration fails.
     */
    public AuthenticationResponse register(RegisterDto registerDto) {
        boolean mailExists;

        try {
            mailExists = userRepository.existsByEmail(registerDto.getEmail());
        } catch (PersistenceException e) {
            log.error("Could not called existByEmail on the database.");
            log.error("Database is not reachable, {}", e.getMessage());
            return null;
        }

        if (mailExists) {
            log.error("Mail could not found in database for: {}", registerDto.getEmail());
            return null;
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

        String jwtToken;

        try {
            jwtToken = jwtService.generateToken(user);
        } catch (PersistenceException e) {
            log.error("Could not save user on the database.");
            log.error("Database is not reachable, {}", e.getMessage());
            return null;
        }

        log.info("Saving token to the database.");
        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    /**
     * Authenticates an existing user with the provided authentication information.
     *
     * @param authenticationDto the authentication data transfer object containing user authentication information.
     * @return an authentication response containing a JWT token, or null if authentication fails.
     */
    public AuthenticationResponse authenticate(AuthenticationDto authenticationDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationDto.getEmail(), authenticationDto.getPassword()));

        log.info("Trying to find user with email.");

        User user;

        try {
            user = this.userRepository.findByEmail(authenticationDto.getEmail()).orElseThrow();
        } catch (NoSuchElementException nse) {
            log.error("User with this email does not exist in the database.");
            return null;
        } catch (PersistenceException e) {
            log.error("Could not called findByEmail on the database.");
            log.error("Database is not reachable, {}", e.getMessage());
            return null;
        }

        log.info("Generating token.");
        String jwtToken = jwtService.generateToken(user);

        log.info("Revoking other tokens.");
        revokeAllUserTokens(user);

        log.info("Saving tokens to the database.");
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder().token(jwtToken).build();
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
