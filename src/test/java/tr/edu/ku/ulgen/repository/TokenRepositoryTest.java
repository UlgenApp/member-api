package tr.edu.ku.ulgen.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import tr.edu.ku.ulgen.entity.Role;
import tr.edu.ku.ulgen.entity.Token;
import tr.edu.ku.ulgen.entity.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void setUp() {
        testUser = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        userRepository.save(testUser);
    }

    @Test
    public void shouldReturnValidUserTokens() {

        // Given

        Token validToken1 = Token.builder()
                .token("validToken1")
                .user(testUser)
                .revoked(false)
                .expired(false)
                .build();

        Token validToken2 = Token.builder()
                .token("validToken2")
                .user(testUser)
                .revoked(false)
                .expired(false)
                .build();

        Token invalidToken1 = Token.builder()
                .token("invalidToken1")
                .user(testUser)
                .revoked(true)
                .expired(false)
                .build();

        Token invalidToken2 = Token.builder()
                .token("invalidToken2")
                .user(testUser)
                .revoked(false)
                .expired(true)
                .build();

        tokenRepository.save(validToken1);
        tokenRepository.save(validToken2);
        tokenRepository.save(invalidToken1);
        tokenRepository.save(invalidToken2);

        // When
        List<Token> validTokens = tokenRepository.findAllValidTokenByUser(testUser.getId());

        // Then
        assertThat(validTokens)
                .hasSize(2)
                .extracting("id")
                .containsExactlyInAnyOrder(validToken1.getId(), validToken2.getId());
    }
}
