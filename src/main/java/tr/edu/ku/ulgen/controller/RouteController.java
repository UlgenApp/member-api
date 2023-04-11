package tr.edu.ku.ulgen.controller;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.edu.ku.ulgen.dto.ProducerDto;
import tr.edu.ku.ulgen.dto.RouteDto;
import tr.edu.ku.ulgen.response.AffectedCitiesResponse;
import tr.edu.ku.ulgen.service.AffectedDataService;
import tr.edu.ku.ulgen.service.RouteService;
import tr.edu.ku.ulgen.service.UlgenConfigService;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
@Slf4j
public class RouteController {
    private RouteService routeService;
    private AffectedDataService affectedDataService;
    private UlgenConfigService ulgenConfigService;

    @PostMapping("/route")
    public ResponseEntity<?> route(@RequestBody RouteDto routeDto) {

        try {
            return ResponseEntity.ok(routeService.route(routeDto));
        } catch (FeignException e) {
            log.error("FeignException occured, routing-api is down: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service is currently unavailable");
        }
    }

    @GetMapping("/affected-cities")
    public ResponseEntity<?> getAffectedCities() {
        Boolean isAlerted = ulgenConfigService.isAlerted();

        if (isAlerted == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service is currently unavailable");
        }

        if (isAlerted) {
            return ResponseEntity.ok(AffectedCitiesResponse.builder()
                    .affectedCities(affectedDataService.getAffectedCities())
                    .build());
        }

        return ResponseEntity.badRequest().build();
    }
}
