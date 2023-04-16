package tr.edu.ku.ulgen.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * A class that represents the response object containing a list of affected cities.
 *
 * @author Kaan Turkmen
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AffectedCitiesResponse {
    private List<String> affectedCities;
}
