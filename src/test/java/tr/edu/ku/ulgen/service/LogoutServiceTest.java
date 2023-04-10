package tr.edu.ku.ulgen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tr.edu.ku.ulgen.entity.Token;
import tr.edu.ku.ulgen.repository.TokenRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogoutServiceTest {

    @InjectMocks
    private LogoutService logoutService;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Authentication authentication;

    private final String sampleToken = "sample_jwt_token";

    @BeforeEach
    public void setUp() {
        String authHeader = "Bearer " + sampleToken;
        when(request.getHeader("Authorization")).thenReturn(authHeader);
    }

    @Test
    public void shouldLogoutSuccessfully() {

        // Given
        Token storedToken = new Token();
        storedToken.setToken(sampleToken);
        when(tokenRepository.findByToken(sampleToken)).thenReturn(Optional.of(storedToken));

        // When
        logoutService.logout(request, response, authentication);

        // Then
        verify(tokenRepository).save(storedToken);
    }

    @Test
    public void shouldNotLogoutWhenTokenNotFound() {

        // Given
        when(tokenRepository.findByToken(sampleToken)).thenReturn(Optional.empty());

        // When
        logoutService.logout(request, response, authentication);

        // Then
        verify(tokenRepository, never()).save(any(Token.class));
    }
}
