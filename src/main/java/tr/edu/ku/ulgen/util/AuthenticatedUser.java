package tr.edu.ku.ulgen.util;

import lombok.Data;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import tr.edu.ku.ulgen.entity.User;
import tr.edu.ku.ulgen.repository.UserRepository;

@Data
public class AuthenticatedUser {
    private final UserRepository userRepository;

    public User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username = "";

        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return userRepository.findByEmail(username).orElse(null);
    }

}
