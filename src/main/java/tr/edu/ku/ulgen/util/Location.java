package tr.edu.ku.ulgen.util;

import lombok.Builder;
import lombok.Data;

/**
 * Location is a utility class that represents a geographic location
 * with latitude and longitude coordinates.
 *
 * @author Kaan Turkmen
 */
@Data
@Builder
public class Location {
    private Double latitude;
    private Double longitude;
}
