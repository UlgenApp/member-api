package tr.edu.ku.ulgen.dto;

import lombok.Data;

import java.util.List;

/**
 * Data transfer object for affected cities.
 * Represents a list of cities and their affected status.
 *
 * @author Kaan Turkmen
 */
@Data
public class AffectedCitiesDto {
    private List<String> cities;
    private Boolean affected;
}
