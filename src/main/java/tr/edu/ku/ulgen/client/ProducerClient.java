package tr.edu.ku.ulgen.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tr.edu.ku.ulgen.dto.ProducerDto;

@FeignClient(name = "producer", url = "${producer.client.url}")
public interface ProducerClient {

    @PostMapping("/api/v1/producer/produce")
    ResponseEntity<?> produceData(@RequestBody ProducerDto request);
}
