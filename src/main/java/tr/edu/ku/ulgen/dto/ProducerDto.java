package tr.edu.ku.ulgen.dto;

import lombok.Builder;
import lombok.Data;
import tr.edu.ku.ulgen.util.Location;

@Data
@Builder
public class ProducerDto {
    private Long userId;
    private Location location;
    private Integer activeUser;
    private String userCity;
}