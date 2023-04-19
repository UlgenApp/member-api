package tr.edu.ku.ulgen.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A response object representing the result of an authentication request.
 *
 * @author Kaan Turkmen
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String result;
    private String token;
}
