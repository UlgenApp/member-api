package tr.edu.ku.ulgen.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProducerDto {
    private Long userId;
    private Long[] location;
    private Integer activeUser;
    private String userCity;
}