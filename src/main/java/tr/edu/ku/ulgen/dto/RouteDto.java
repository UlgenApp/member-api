package tr.edu.ku.ulgen.dto;

import lombok.Builder;
import lombok.Data;
import tr.edu.ku.ulgen.util.Location;

import java.util.List;

/**
 * Data transfer object for route information.
 * Represents the route parameters, including epsilon, priority and distance coefficients, vehicle count, depot, and cities.
 *
 * @author Kaan Turkmen
 */
@Data
@Builder
public class RouteDto {
    private Double epsilon;
    private Double priority_coefficient;
    private Double distance_coefficient;
    private Integer vehicleCount;
    private Location depot;
    private List<String> cities;
}
