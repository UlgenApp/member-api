package tr.edu.ku.ulgen.dto;

import lombok.Builder;
import lombok.Data;
import tr.edu.ku.ulgen.util.Location;

import java.util.List;

@Data
@Builder
public class RouteDataDto {
    private Double epsilon;
    private Integer vehicle_count;
    private Location depot;
    private List<Location> location;
}