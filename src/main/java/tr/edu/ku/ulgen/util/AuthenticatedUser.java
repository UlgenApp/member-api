package tr.edu.ku.ulgen.util;

import jakarta.persistence.PersistenceException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import tr.edu.ku.ulgen.entity.User;
import tr.edu.ku.ulgen.repository.UserRepository;

/**
 * AuthenticatedUser is a utility class that retrieves the authenticated user
 * from the Spring Security context and fetches the corresponding User entity
 * from the UserRepository.
 *
 * @author Kaan Turkmen
 */
@Slf4j
@Data
public class AuthenticatedUser {
    private final UserRepository userRepository;

    /**
     * Retrieves the authenticated User entity from the UserRepository based on
     * the username stored in the Spring Security context.
     *
     * @return The authenticated User entity, or null if the user is not found.
     */
    public User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = "";

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        try {
            return userRepository.findByEmail(username).orElse(null);
        } catch (PersistenceException e) {
            log.error("Could not execute find by email on the database.");
            log.error("Database is not reachable, {}", e.getMessage());

            return null;
        }
    }

}
