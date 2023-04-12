package tr.edu.ku.ulgen.dto;

import lombok.Data;
import tr.edu.ku.ulgen.util.Location;

import java.util.List;

@Data
public class ProducerDataDto {
    private Location location;
    private List<String> macAddresses;
    private String userCity;
}
