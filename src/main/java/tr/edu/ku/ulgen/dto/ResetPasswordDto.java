package tr.edu.ku.ulgen.dto;

import lombok.Data;

/**
 * Data transfer object for resetting password.
 * Represents the token and new password.
 *
 * @author Kaan Turkmen
 */
@Data
public class ResetPasswordDto {
    private String token;
    private String newPassword;
}
