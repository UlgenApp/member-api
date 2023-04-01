package tr.edu.ku.ulgen.dto;

import lombok.Data;

@Data
public class ProducerDataDto {
    private Long[] location;
    private Integer activeUser;
    private String userCity;
}
