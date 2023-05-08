package tr.edu.ku.ulgen.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Data transfer object for heatmap information.
 * Represents the route parameters, including epsilon, and location strings.
 *
 * @author Kaan Turkmen
 */
@Data
@Builder
public class HeatmapDto {
    private Double epsilon;
    private List<String> cities;
}
