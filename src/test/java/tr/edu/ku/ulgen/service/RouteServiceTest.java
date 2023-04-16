package tr.edu.ku.ulgen.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import tr.edu.ku.ulgen.client.RouterClient;
import tr.edu.ku.ulgen.dto.RouteDto;
import tr.edu.ku.ulgen.entity.UlgenData;
import tr.edu.ku.ulgen.repository.UlgenDataRepository;
import tr.edu.ku.ulgen.util.Location;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RouteServiceTest {

    @InjectMocks
    private RouteService routeService;

    @Mock
    private RouterClient routerClient;

    @Mock
    private UlgenDataRepository ulgenDataRepository;

    @Mock
    private UlgenConfigService ulgenConfigService;

    @Mock
    private AffectedDataService affectedDataService;

    @Test
    public void shouldReturnRoute() throws JsonProcessingException {

        // Given
        RouteDto routeDto = RouteDto.builder()
                .cities(Arrays.asList("City1", "City2"))
                .epsilon(0.1)
                .distance_coefficient(0.3)
                .priority_coefficient(0.7)
                .vehicleCount(2)
                .depot(Location.builder().latitude(40.0).longitude(29.0).build())
                .build();

        List<UlgenData> ulgenDataList = Arrays.asList(
                UlgenData.builder().userId(1L).activeUsers(10).latitude(40.1).longitude(29.1).userCity("City1").build(),
                UlgenData.builder().userId(2L).activeUsers(5).latitude(40.2).longitude(29.2).userCity("City2").build()
        );

        when(ulgenDataRepository.findByUserCityIn(routeDto.getCities())).thenReturn(ulgenDataList);

        when(ulgenConfigService.isAlerted()).thenReturn(true);

        when(affectedDataService.getAffectedCities()).thenReturn(routeDto.getCities());

        ObjectMapper objectMapper = new ObjectMapper();
        String routerResponseJson = "{\"result\": \"sample data\"}";
        JsonNode routerResponseJsonNode = objectMapper.readTree(routerResponseJson);
        when(routerClient.route(any())).thenReturn(ResponseEntity.ok(routerResponseJsonNode));

        // When
        ResponseEntity<?> response = routeService.route(routeDto);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getBody()).isEqualTo(routerResponseJsonNode);
    }
}
