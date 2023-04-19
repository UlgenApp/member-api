package tr.edu.ku.ulgen.response;

import lombok.Builder;
import lombok.Data;

/**
 * A response object representing the result of verifying the email request.
 *
 * @author Kaan Turkmen
 */
@Builder
@Data
public class VerifyEmailResponse {
    private String result;
}
