package tr.edu.ku.ulgen.dto;

import lombok.Builder;
import lombok.Data;
import tr.edu.ku.ulgen.util.Location;

import java.util.List;

@Data
@Builder
public class RouteDto {
    private Double epsilon;
    private Integer vehicleCount;
    private Location depot;
    private List<String> cities;
}