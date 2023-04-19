package tr.edu.ku.ulgen.dto;

import lombok.Data;

/**
 * Data transfer object for resending the email verification.
 * Represents the email.
 *
 * @author Kaan Turkmen
 */
@Data
public class ResendEmailVerificationDto {
    private String email;
}
