package tr.edu.ku.ulgen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import tr.edu.ku.ulgen.dto.AuthenticationDto;
import tr.edu.ku.ulgen.dto.RegisterDto;
import tr.edu.ku.ulgen.entity.Role;
import tr.edu.ku.ulgen.entity.User;
import tr.edu.ku.ulgen.repository.TokenRepository;
import tr.edu.ku.ulgen.repository.UserRepository;
import tr.edu.ku.ulgen.response.*;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private EmailSenderService emailSenderService;

    private RegisterDto registerDto;
    private User user;

    @BeforeEach
    void setUp() {
        registerDto = RegisterDto.builder()
                .email("test@example.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .build();

        user = User.builder()
                .email(registerDto.getEmail())
                .password(registerDto.getPassword())
                .firstName(registerDto.getFirstName())
                .lastName(registerDto.getLastName())
                .role(Role.USER)
                .build();
    }

    @Test
    void shouldRegister() {
        // Given
        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(Map.class), any(User.class))).thenReturn("test_jwt_token");

        // When
        ResponseEntity<RegisterResponse> response = authenticationService.register(registerDto);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getResult()).isEqualTo("SUCCESS");
        verify(emailSenderService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void shouldGiveEmailIsTaken() {
        // Given
        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(true);

        // When
        ResponseEntity<RegisterResponse> response = authenticationService.register(registerDto);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getResult()).isEqualTo("EMAIL_IS_TAKEN");
    }

    @Test
    public void shouldAuthenticate() {
        // Given
        AuthenticationDto authenticationDto = new AuthenticationDto("test@example.com", "test_password");
        User user = new User(1L, "John", "Doe", "test@example.com", passwordEncoder.encode("test_password"), Role.USER, null, true);
        when(userRepository.findByEmail(authenticationDto.getEmail())).thenReturn(Optional.of(user));

        // When
        ResponseEntity<AuthenticationResponse> response = authenticationService.authenticate(authenticationDto);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getResult()).isEqualTo("SUCCESS");
    }


    @Test
    public void shouldTestResendEmailVerification() {
        // Given
        String email = "test@example.com";
        User user = new User(1L, "John", "Doe", email, "hashed_password", Role.USER, null, false);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        ResponseEntity<VerifyEmailResponse> response = authenticationService.resendEmailVerification(email);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getResult()).isEqualTo("SUCCESS");
    }

    @Test
    public void shouldTestForgotPassword() {
        // Given
        String email = "test@example.com";
        User user = new User(1L, "John", "Doe", email, "hashed_password", Role.USER, null, true);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        ResponseEntity<ForgotPasswordResponse> response = authenticationService.forgotPassword(email);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getResult()).isEqualTo("SUCCESS");
    }

    @Test
    public void shouldTestResetPassword() {
        // Given
        String token = "valid_password_reset_token";
        String newPassword = "new_test_password";
        String email = "test@example.com";
        User user = new User(1L, "John", "Doe", email, "hashed_password", Role.USER, null, true);
        when(jwtService.hasClaim(token, "password_reset")).thenReturn(true);
        when(jwtService.extractUsername(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // When
        ResponseEntity<ResetPasswordResponse> response = authenticationService.resetPassword(token, newPassword);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getResult()).isEqualTo("SUCCESS");
    }

}
