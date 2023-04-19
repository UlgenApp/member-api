package tr.edu.ku.ulgen.dto;

import lombok.Data;

/**
 * Data transfer object for forgetting password data.
 * Represents the email.
 *
 * @author Kaan Turkmen
 */
@Data
public class ForgotPasswordDto {
    private String email;
}
