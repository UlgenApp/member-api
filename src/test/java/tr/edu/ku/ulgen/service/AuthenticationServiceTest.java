package tr.edu.ku.ulgen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import tr.edu.ku.ulgen.dto.AuthenticationDto;
import tr.edu.ku.ulgen.dto.RegisterDto;
import tr.edu.ku.ulgen.entity.Role;
import tr.edu.ku.ulgen.entity.User;
import tr.edu.ku.ulgen.repository.TokenRepository;
import tr.edu.ku.ulgen.repository.UserRepository;
import tr.edu.ku.ulgen.response.AuthenticationResponse;

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
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    private RegisterDto registerDto;
    private AuthenticationDto authenticationDto;

    @BeforeEach
    public void setUp() {
        registerDto = new RegisterDto("John", "Doe", "john.doe@example.com", "testpassword", "Hello from Ulgen!");
        authenticationDto = new AuthenticationDto("john.doe@example.com", "testpassword");
    }

    @Test
    public void shouldRegisterSuccessful() {
        // Given
        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtService.generateToken(any(User.class))).thenReturn("sample_jwt_token");

        // When
        AuthenticationResponse response = authenticationService.register(registerDto);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("sample_jwt_token");

        verify(userRepository).save(any(User.class));
        verify(tokenRepository).save(any());
    }

    @Test
    public void shouldAuthenticateUser() {
        // Given
        User testUser = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("testpassword")
                .role(Role.USER)
                .additionalInfo("Hello from Ulgen!")
                .build();

        when(userRepository.findByEmail(authenticationDto.getEmail())).thenReturn(Optional.of(testUser));
        when(jwtService.generateToken(testUser)).thenReturn("sample_jwt_token");

        // When
        AuthenticationResponse response = authenticationService.authenticate(authenticationDto);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("sample_jwt_token");

        verify(tokenRepository).save(any());
        verify(tokenRepository).findAllValidTokenByUser(testUser.getId());
    }

    @Test
    public void shouldReturnNullBecauseOfTakenEmail() {
        // Given
        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(true);

        // When
        AuthenticationResponse response = authenticationService.register(registerDto);

        // Then
        assertThat(response).isNull();

        verify(userRepository, never()).save(any(User.class));
        verify(tokenRepository, never()).save(any());
    }

}
