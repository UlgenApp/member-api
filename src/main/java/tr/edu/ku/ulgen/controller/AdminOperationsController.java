package tr.edu.ku.ulgen.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.edu.ku.ulgen.dto.AlertedDto;
import tr.edu.ku.ulgen.service.AffectedDataService;
import tr.edu.ku.ulgen.service.UlgenConfigService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminOperationsController {

    private final UlgenConfigService ulgenConfigService;
    private final AffectedDataService affectedDataService;

    @PutMapping("/set-alerted")
    public ResponseEntity<Void> setAlertedValue(AlertedDto alertedDto) {
        ulgenConfigService.setConfigPropertyValue("alerted", alertedDto.getAlerted());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/set-affected-cities")
    public ResponseEntity<Void> setAffectedCities(@RequestBody List<String> cityNames, @RequestParam("value") Boolean value) {
        affectedDataService.setAffectedCities(cityNames, value);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
