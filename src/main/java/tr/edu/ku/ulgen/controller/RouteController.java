package tr.edu.ku.ulgen.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.edu.ku.ulgen.dto.RouteDto;
import tr.edu.ku.ulgen.response.AffectedCitiesResponse;
import tr.edu.ku.ulgen.service.AffectedDataService;
import tr.edu.ku.ulgen.service.RouteService;
import tr.edu.ku.ulgen.service.UlgenConfigService;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class RouteController {
    private RouteService routeService;
    private AffectedDataService affectedDataService;
    private UlgenConfigService ulgenConfigService;

    @PostMapping("/route")
    public ResponseEntity<?> route(@RequestBody RouteDto routeDto) {

        return ResponseEntity.ok(routeService.route(routeDto));
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
