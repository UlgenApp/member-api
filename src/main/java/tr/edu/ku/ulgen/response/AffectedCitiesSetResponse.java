package tr.edu.ku.ulgen.response;

import lombok.Builder;
import lombok.Data;

/**
 * A response object representing the response object containing a set response of affected cities.
 *
 * @author Kaan Turkmen
 */
@Builder
@Data
public class AffectedCitiesSetResponse {
    private String result;
}
