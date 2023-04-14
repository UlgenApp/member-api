package tr.edu.ku.ulgen.client.fallback;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import tr.edu.ku.ulgen.client.ProducerClient;
import tr.edu.ku.ulgen.client.RouterClient;
import tr.edu.ku.ulgen.dto.ProducerDto;
import tr.edu.ku.ulgen.dto.RouteDataDto;

@Component
public class FeignClientFallback implements ProducerClient, RouterClient {

    @Override
    public ResponseEntity<?> produceData(ProducerDto producerDto) {
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Override
    public ResponseEntity<JsonNode> route(RouteDataDto routeDataDto) {
        return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
    }
}