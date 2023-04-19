package tr.edu.ku.ulgen.response;

import lombok.Builder;
import lombok.Data;

/**
 * A response object representing the result of a user profile.
 *
 * @author Kaan Turkmen
 */
@Builder
@Data
public class UserResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String additionalInfo;
}
