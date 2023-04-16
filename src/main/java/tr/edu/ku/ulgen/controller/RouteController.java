package tr.edu.ku.ulgen.controller;

import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.edu.ku.ulgen.dto.RouteDto;
import tr.edu.ku.ulgen.response.AffectedCitiesResponse;
import tr.edu.ku.ulgen.service.AffectedDataService;
import tr.edu.ku.ulgen.service.RouteService;
import tr.edu.ku.ulgen.service.UlgenConfigService;

/**
 * REST controller for handling user route requests and fetching affected cities.
 * This class exposes API endpoints for getting routing information and retrieving affected cities.
 *
 * @author Kaan Turkmen
 */
@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
@Slf4j
public class RouteController {
    private final RouteService routeService;
    private final AffectedDataService affectedDataService;
    private final UlgenConfigService ulgenConfigService;

    /**
     * Retrieves the routing information based on the provided {@link RouteDto}.
     * In case of a FeignException, returns a service unavailable status.
     *
     * @param routeDto the {@link RouteDto} object containing the route request details.
     * @return a {@link ResponseEntity} containing the result of the routing operation.
     */
    @PostMapping("/route")
    public ResponseEntity<?> route(@RequestBody RouteDto routeDto) {

        try {
            return ResponseEntity.ok(routeService.route(routeDto));
        } catch (FeignException e) {
            log.error("FeignException occured, routing-api is down: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service is currently unavailable");
        }
    }

    /**
     * Retrieves the affected cities if the system is in alerted state.
     * If the system is not in alerted state, returns a bad request status.
     *
     * @return a {@link ResponseEntity} containing the {@link AffectedCitiesResponse} with the affected cities
     * or a bad request status if the system is not in alerted state.
     */
    @GetMapping("/affected-cities")
    public ResponseEntity<?> getAffectedCities() {
        Boolean isAlerted = ulgenConfigService.isAlerted();

        if (isAlerted == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service is currently unavailable");
        }

        if (isAlerted) {
            return ResponseEntity.ok(AffectedCitiesResponse.builder().affectedCities(affectedDataService.getAffectedCities()).build());
        }

        return ResponseEntity.badRequest().build();
    }
}
