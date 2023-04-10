package tr.edu.ku.ulgen.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.edu.ku.ulgen.dto.AffectedCitiesDto;
import tr.edu.ku.ulgen.dto.AlertedDto;
import tr.edu.ku.ulgen.service.AffectedDataService;
import tr.edu.ku.ulgen.service.UlgenConfigService;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminOperationsController {

    private final UlgenConfigService ulgenConfigService;
    private final AffectedDataService affectedDataService;

    @PostMapping("/set-alerted")
    public ResponseEntity<?> setAlertedValue(@RequestBody AlertedDto alertedDto) {
        log.info("Set Alerted endpoint has been called: {}", alertedDto);
        ulgenConfigService.setConfigPropertyValue("alerted", alertedDto.getAlerted());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/set-affected-cities")
    public ResponseEntity<?> setAffectedCities(@RequestBody AffectedCitiesDto affectedCitiesDto) {
        affectedDataService.setAffectedCities(affectedCitiesDto.getCities(), affectedCitiesDto.getAffected());
        return ResponseEntity.ok().build();
    }
}
