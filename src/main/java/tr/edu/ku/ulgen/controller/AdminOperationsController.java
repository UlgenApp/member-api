package tr.edu.ku.ulgen.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<Void> setAlertedValue(AlertedDto alertedDto) {
        ulgenConfigService.setConfigPropertyValue("alerted", alertedDto.getAlerted());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/set-affected-cities")
    public ResponseEntity<Void> setAffectedCities(AffectedCitiesDto affectedCitiesDto) {
        affectedDataService.setAffectedCities(affectedCitiesDto.getCityNames(), affectedCitiesDto.getValue());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
