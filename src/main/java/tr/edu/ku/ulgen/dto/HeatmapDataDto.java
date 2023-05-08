package tr.edu.ku.ulgen.dto;

import lombok.Builder;
import lombok.Data;
import tr.edu.ku.ulgen.util.Location;

import java.util.List;

/**
 * Data transfer object for heatmap data.
 * Represents the route parameters, including epsilon, and locations.
 *
 * @author Kaan Turkmen
 */
@Data
@Builder
public class HeatmapDataDto {
    private Double epsilon;
    private List<Location> location;
}
