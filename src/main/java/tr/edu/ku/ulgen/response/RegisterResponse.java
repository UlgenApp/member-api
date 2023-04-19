package tr.edu.ku.ulgen.response;

import lombok.Builder;
import lombok.Data;

/**
 * A response object representing the response object containing a result of register request.
 *
 * @author Kaan Turkmen
 */
@Builder
@Data
public class RegisterResponse {
    private String result;
}
