package tr.edu.ku.ulgen.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tr.edu.ku.ulgen.client.fallback.FeignClientFallback;
import tr.edu.ku.ulgen.config.FeignClientConfiguration;
import tr.edu.ku.ulgen.dto.HeatmapDataDto;
import tr.edu.ku.ulgen.dto.RouteDataDto;

/**
 * Feign client for interacting with the router service API.
 * This client sends HTTP requests to the router API, which is responsible for processing route data.
 *
 * @author Kaan Turkmen
 */
@FeignClient(name = "router", url = "${router.client.url}", configuration = FeignClientConfiguration.class, fallback = FeignClientFallback.class)
public interface RouterClient {

    /**
     * Sends a request to the router service API to process route data.
     *
     * @param routeDataDto the route data transfer object containing the required route information.
     * @return a {@link ResponseEntity} containing a {@link JsonNode} with the processed route data.
     */
    @PostMapping("/api/v1/route")
    ResponseEntity<JsonNode> route(@RequestBody RouteDataDto routeDataDto);

    /**
     * Sends a request to the router service API to process heatmap data.
     *
     * @param heatmapDataDto the heatmap data transfer object containing the required heatmap information.
     * @return a {@link ResponseEntity} containing a {@link JsonNode} with the processed route data.
     */
    @PostMapping("/api/v1/heatmap")
    ResponseEntity<JsonNode> heatmap(@RequestBody HeatmapDataDto heatmapDataDto);
}
