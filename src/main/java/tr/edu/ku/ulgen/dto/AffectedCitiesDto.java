package tr.edu.ku.ulgen.dto;

import lombok.Data;

import java.util.List;

@Data
public class AffectedCitiesDto {
    private List<String> cityNames;
    private Boolean value;
}
