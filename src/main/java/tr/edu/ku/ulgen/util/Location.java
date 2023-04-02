package tr.edu.ku.ulgen.util;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Location {
    private Double latitude;
    private Double longitude;
}
