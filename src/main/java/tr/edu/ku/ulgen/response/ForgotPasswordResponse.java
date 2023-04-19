package tr.edu.ku.ulgen.response;

import lombok.Builder;
import lombok.Data;

/**
 * A response object representing the result of a forgot password request.
 *
 * @author Kaan Turkmen
 */
@Builder
@Data
public class ForgotPasswordResponse {
    private String result;
}
