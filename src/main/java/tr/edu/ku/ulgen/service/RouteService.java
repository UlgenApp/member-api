package tr.edu.ku.ulgen.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public ResponseEntity<?> route(RouteDto routeDto) {
        List<String> cities = routeDto.getCities();
        List<Location> affectedLocations = ulgenDataRepository.findByUserCityIn(cities)
                .stream()
                .map(ulgenData -> Location.builder()
                        .latitude(ulgenData.getLatitude())
                        .longitude(ulgenData.getLongitude()).build())
                .toList();

        return ResponseEntity.ok(routerClient.route(RouteDataDto.builder()
                .epsilon(routeDto.getEpsilon())
                .vehicle_count(routeDto.getVehicleCount())
                .depot(routeDto.getDepot())
                .location(affectedLocations)
                .build()));
    }
}
