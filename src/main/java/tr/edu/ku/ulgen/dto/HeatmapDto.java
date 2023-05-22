package tr.edu.ku.ulgen.dto;

import lombok.Data;

/**
 * Data transfer object for heatmap information.
 * Represents the route parameters, including epsilon, and location strings.
 *
 * @author Kaan Turkmen
 */
@Data
public class HeatmapDto {
    private Double epsilon;
}
