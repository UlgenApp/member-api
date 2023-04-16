package tr.edu.ku.ulgen.dto;

import lombok.Builder;
import lombok.Data;
import tr.edu.ku.ulgen.util.Location;

import java.util.List;

/**
 * Data transfer object for route data.
 * Represents the route parameters, including epsilon, priority and distance coefficients, vehicle count, depot, and locations.
 *
 * @author Kaan Turkmen
 */
@Data
@Builder
public class RouteDataDto {
    private Double epsilon;
    private Double priority_coefficient;
    private Double distance_coefficient;
    private Integer vehicle_count;
    private Location depot;
    private List<Location> location;
}
