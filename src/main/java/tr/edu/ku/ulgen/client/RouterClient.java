package tr.edu.ku.ulgen.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tr.edu.ku.ulgen.dto.RouteDataDto;

@FeignClient(name = "router", url = "${router.client.url}")
public interface RouterClient {

    @PostMapping("/api/v1/route")
    ResponseEntity<?> route(@RequestBody RouteDataDto routeDataDto);
}
