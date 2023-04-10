package tr.edu.ku.ulgen.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        String SECRET_KEY = "2F413F4428472B4B6150645367566B5970337336763979244226452948404D63";
        ReflectionTestUtils.setField(jwtService, "SECRET_KEY", SECRET_KEY);
    }

    @Test
    public void shouldGenerateToken() {

        // Given
        String username = "testUser";
        when(userDetails.getUsername()).thenReturn(username);

        // When
        String token = jwtService.generateToken(userDetails);

        // Then
        assertThat(token).isNotBlank();
        String extractedUsername = jwtService.extractUsername(token);
        assertThat(extractedUsername).isEqualTo(username);
    }

    @Test
    public void shouldTakenBeValid() {

        // Given
        String username = "testUser";
        when(userDetails.getUsername()).thenReturn(username);

        String token = jwtService.generateToken(userDetails);

        // When
        boolean isValid = jwtService.isTokenValid(token, userDetails);

        // Then
        assertThat(isValid).isTrue();
    }
}
