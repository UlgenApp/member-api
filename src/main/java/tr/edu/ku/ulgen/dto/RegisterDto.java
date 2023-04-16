package tr.edu.ku.ulgen.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object for user registration.
 * Represents the user's first name, last name, email, password, and additional information.
 *
 * @author Kaan Turkmen
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String additionalInfo;
}
