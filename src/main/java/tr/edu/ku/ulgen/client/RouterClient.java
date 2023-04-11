package tr.edu.ku.ulgen.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tr.edu.ku.ulgen.client.fallback.FeignClientFallback;
import tr.edu.ku.ulgen.config.FeignClientConfiguration;
import tr.edu.ku.ulgen.dto.RouteDataDto;

@FeignClient(name = "router", url = "${router.client.url}", configuration = FeignClientConfiguration.class, fallback = FeignClientFallback.class)
public interface RouterClient {

    @PostMapping("/api/v1/route")
    ResponseEntity<JsonNode> route(@RequestBody RouteDataDto routeDataDto);
}
