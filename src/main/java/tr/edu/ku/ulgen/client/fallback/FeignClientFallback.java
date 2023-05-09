package tr.edu.ku.ulgen.client.fallback;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import tr.edu.ku.ulgen.client.ProducerClient;
import tr.edu.ku.ulgen.client.RouterClient;
import tr.edu.ku.ulgen.dto.HeatmapDataDto;
import tr.edu.ku.ulgen.dto.ProducerDto;
import tr.edu.ku.ulgen.dto.RouteDataDto;

/**
 * A fallback implementation for the {@link ProducerClient} and {@link RouterClient} interfaces.
 * This fallback is used when Feign clients are unable to connect to their respective services.
 *
 * @author Kaan Turkmen
 */
@Component
public class FeignClientFallback implements ProducerClient, RouterClient {

    /**
     * Fallback method for the producer client.
     *
     * @param producerDto the producer data transfer object containing the required producer information
     * @return a {@link ResponseEntity} with a {@link HttpStatus#SERVICE_UNAVAILABLE} status
     */
    @Override
    public ResponseEntity<?> produceData(ProducerDto producerDto) {
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    /**
     * Fallback method for the router client.
     *
     * @param routeDataDto the route data transfer object containing the required route information
     * @return a {@link ResponseEntity} with a {@link HttpStatus#SERVICE_UNAVAILABLE} status
     */
    @Override
    public ResponseEntity<JsonNode> route(RouteDataDto routeDataDto) {
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    /**
     * Fallback method for the router client.
     *
     * @param heatmapDataDto the heatmap data transfer object containing the required route information,
     * @return a {@link ResponseEntity} with a {@link HttpStatus#SERVICE_UNAVAILABLE} status.
     */
    @Override
    public ResponseEntity<JsonNode> heatmap(HeatmapDataDto heatmapDataDto) {
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }
}