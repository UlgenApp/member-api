package tr.edu.ku.ulgen.dto;

import lombok.Builder;
import lombok.Data;
import tr.edu.ku.ulgen.util.Location;

/**
 * Data transfer object for producer data.
 * Represents the user ID, location, number of active users, and user city for data production.
 *
 * @author Kaan Turkmen
 */
@Data
@Builder
public class ProducerDto {
    private Long userId;
    private Location location;
    private Integer activeUser;
    private String userCity;
}