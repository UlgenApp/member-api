package tr.edu.ku.ulgen.dto;

import lombok.Data;
import tr.edu.ku.ulgen.util.Location;

import java.util.List;

/**
 * Data transfer object for producer data.
 * Represents the location, MAC addresses, and user city for data production.
 *
 * @author Kaan Turkmen
 */
@Data
public class ProducerDataDto {
    private Location location;
    private List<String> macAddresses;
    private String userCity;
}
