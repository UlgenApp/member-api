package tr.edu.ku.ulgen.service;

import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import tr.edu.ku.ulgen.entity.Token;
import tr.edu.ku.ulgen.repository.TokenRepository;

/**
 * A service class provides service-level operations related to the logout process for users.
 * The main responsibility of this class is to interact with the TokenRepository and
 * handle the logout process by expiring and revoking user tokens.
 *
 * @author Kaan Turkmen
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    /**
     * Handles the logout process for a user by expiring and revoking their token.
     *
     * @param request        The HttpServletRequest containing the user's authentication header.
     * @param response       The HttpServletResponse to be sent back to the user.
     * @param authentication The Authentication object containing the user's authentication details.
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.error("There is no bearer on the header.");
            return;
        }
        jwt = authHeader.substring(7);

        log.info("Trying to find stored token.");

        Token storedToken;

        try {
            storedToken = tokenRepository.findByToken(jwt).orElse(null);
        } catch (PersistenceException e) {
            log.error("Could not called findByToken on the database.");
            log.error("Database is not reachable, {}", e.getMessage());
            return;
        }

        if (storedToken != null) {
            log.info("Revoking and expiring the existing token for the user.");
            storedToken.setExpired(true);
            storedToken.setRevoked(true);

            try {
                tokenRepository.save(storedToken);
            } catch (PersistenceException e) {
                log.error("Could not saved token on the database.");
                log.error("Database is not reachable, {}", e.getMessage());
                return;
            }

            SecurityContextHolder.clearContext();
        }
    }
}
