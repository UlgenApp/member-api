package tr.edu.ku.ulgen.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.edu.ku.ulgen.dto.RouteDto;
import tr.edu.ku.ulgen.service.RouteService;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
public class RouteController {
    private RouteService routeService;

    @PostMapping("/route")
    public ResponseEntity<?> route(@RequestBody RouteDto routeDto) {

        return ResponseEntity.ok(routeService.route(routeDto));
    }
}
