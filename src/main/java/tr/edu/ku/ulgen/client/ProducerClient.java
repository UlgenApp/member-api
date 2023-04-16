package tr.edu.ku.ulgen.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tr.edu.ku.ulgen.client.fallback.FeignClientFallback;
import tr.edu.ku.ulgen.config.FeignClientConfiguration;
import tr.edu.ku.ulgen.dto.ProducerDto;

/**
 * Feign client for interacting with the producer service API.
 * This client sends HTTP requests to the producer API, which is responsible for processing producer data.
 *
 * @author Kaan Turkmen
 */
@FeignClient(name = "producer", url = "${producer.client.url}", configuration = FeignClientConfiguration.class, fallback = FeignClientFallback.class)
public interface ProducerClient {

    /**
     * Sends a request to the producer service API to process producer data.
     *
     * @param producerDto the producer data transfer object containing the required producer information.
     * @return a {@link ResponseEntity} containing the processed producer data.
     */
    @PostMapping("/api/v1/producer/produce")
    ResponseEntity<?> produceData(@RequestBody ProducerDto producerDto);
}
