package tr.edu.ku.ulgen.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import tr.edu.ku.ulgen.entity.Role;
import tr.edu.ku.ulgen.repository.UserRepository;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticatedUserTest {

    @InjectMocks
    private AuthenticatedUser authenticatedUser;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private UserDetails userDetails;
    private tr.edu.ku.ulgen.entity.User testUser;

    @BeforeEach
    public void setUp() {
        userDetails = new User("john.doe@example.com", "password", Collections.emptyList());
        testUser = new tr.edu.ku.ulgen.entity.User();
        testUser.setEmail("john.doe@example.com");
        testUser.setRole(Role.USER);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void shouldGetAuthenticatedUser() {

        // Given
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.of(testUser));

        // When
        tr.edu.ku.ulgen.entity.User authenticatedUserResult = authenticatedUser.getAuthenticatedUser();

        // Then
        assertThat(authenticatedUserResult).isNotNull();
        assertThat(authenticatedUserResult.getEmail()).isEqualTo(testUser.getEmail());
        assertThat(authenticatedUserResult.getRole()).isEqualTo(testUser.getRole());
    }

    @Test
    public void shouldReturnNullWhenAuthenticatedUserNotFound() {

        // Given
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.empty());

        // When
        tr.edu.ku.ulgen.entity.User authenticatedUserResult = authenticatedUser.getAuthenticatedUser();

        // Then
        assertThat(authenticatedUserResult).isNull();
    }
}
