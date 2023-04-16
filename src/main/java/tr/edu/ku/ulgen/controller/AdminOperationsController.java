package tr.edu.ku.ulgen.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.edu.ku.ulgen.dto.AffectedCitiesDto;
import tr.edu.ku.ulgen.dto.AlertedDto;
import tr.edu.ku.ulgen.service.AffectedDataService;
import tr.edu.ku.ulgen.service.UlgenConfigService;

/**
 * REST controller for handling admin operations related to configuration settings and affected data.
 * This class exposes API endpoints for updating the "alerted" configuration property and managing affected cities.
 *
 * @author Kaan Turkmen
 */
@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminOperationsController {

    private final UlgenConfigService ulgenConfigService;
    private final AffectedDataService affectedDataService;

    /**
     * Updates the "alerted" configuration property with the provided value.
     *
     * @param alertedDto the {@link AlertedDto} object containing the updated "alerted" value.
     * @return a {@link ResponseEntity} indicating the result of the operation.
     */
    @PostMapping("/set-alerted")
    public ResponseEntity<?> setAlertedValue(@RequestBody AlertedDto alertedDto) {
        log.info("Set Alerted endpoint has been called: {}", alertedDto);
        ulgenConfigService.setConfigPropertyValue("alerted", alertedDto.getAlerted());
        return ResponseEntity.ok().build();
    }

    /**
     * Sets the affected status for the specified cities.
     *
     * @param affectedCitiesDto the {@link AffectedCitiesDto} object containing the list of cities and their affected status.
     * @return a {@link ResponseEntity} indicating the result of the operation.
     */
    @PostMapping("/set-affected-cities")
    public ResponseEntity<?> setAffectedCities(@RequestBody AffectedCitiesDto affectedCitiesDto) {
        affectedDataService.setAffectedCities(affectedCitiesDto.getCities(), affectedCitiesDto.getAffected());
        return ResponseEntity.ok().build();
    }
}
