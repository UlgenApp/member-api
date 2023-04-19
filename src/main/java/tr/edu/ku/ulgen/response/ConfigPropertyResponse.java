package tr.edu.ku.ulgen.response;

import lombok.Builder;
import lombok.Data;

/**
 * A response object representing the response object containing a result of config property request.
 *
 * @author Kaan Turkmen
 */
@Builder
@Data
public class ConfigPropertyResponse {
    private String result;
}
