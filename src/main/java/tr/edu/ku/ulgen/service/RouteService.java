package tr.edu.ku.ulgen.service;

import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tr.edu.ku.ulgen.client.RouterClient;
import tr.edu.ku.ulgen.dto.RouteDataDto;
import tr.edu.ku.ulgen.dto.RouteDto;
import tr.edu.ku.ulgen.repository.UlgenDataRepository;
import tr.edu.ku.ulgen.util.Location;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class RouteService {
    private RouterClient routerClient;
    private UlgenDataRepository ulgenDataRepository;
    private UlgenConfigService ulgenConfigService;
    private AffectedDataService affectedDataService;

    public ResponseEntity<?> route(RouteDto routeDto) {
        Boolean isAlerted = ulgenConfigService.isAlerted();

        if (isAlerted == null) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service is currently unavailable");
        }

        if (!isAlerted) {
            return ResponseEntity.badRequest().build();
        }

        double tolerance = 1e-9;
        double sum = routeDto.getPriority_coefficient() + routeDto.getDistance_coefficient();

        if (Math.abs(sum - 1.0) > tolerance) {
            log.error("Sum of priority and distance coefficients are not 1.");
            return ResponseEntity.unprocessableEntity().build();
        }

        if ((routeDto.getPriority_coefficient() <= 0.0 || routeDto.getPriority_coefficient() >= 1.0) ||
                routeDto.getDistance_coefficient() <= 0.0 || routeDto.getDistance_coefficient() >= 1.0) {
            log.error("Coefficients are not within the valid range (0 < x < 1).");
            return ResponseEntity.unprocessableEntity().build();
        }

        List<String> affectedCities = affectedDataService.getAffectedCities();
        List<String> cities = routeDto.getCities();

        log.info("Affected cities, {}", affectedCities);
        log.info("Cities, {}", cities);

        if (cities.stream().anyMatch(city -> !affectedCities.contains(city))) {
            return ResponseEntity.unprocessableEntity().build();
        }

        List<Location> affectedLocations;

        try {
            affectedLocations = ulgenDataRepository.findByUserCityIn(cities)
                    .stream()
                    .map(ulgenData -> Location.builder()
                            .latitude(ulgenData.getLatitude())
                            .longitude(ulgenData.getLongitude()).build())
                    .toList();
        } catch (PersistenceException e) {
            log.error("Could not called findByUserCityIn on the database.");
            log.error("Database is not reachable, {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service is currently unavailable");
        }

        RouteDataDto routeDataDto = RouteDataDto.builder()
                .epsilon(routeDto.getEpsilon())
                .priority_coefficient(routeDto.getPriority_coefficient())
                .distance_coefficient(routeDto.getDistance_coefficient())
                .vehicle_count(routeDto.getVehicleCount())
                .depot(routeDto.getDepot())
                .location(affectedLocations)
                .build();

        log.info("Fetched database and built: {}", routeDataDto.toString());

        ResponseEntity<?> response = routerClient.route(routeDataDto);

        log.info("Got Response from Router Endpoint: {}", response.toString());

        return response;
    }
}
