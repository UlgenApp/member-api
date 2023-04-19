package tr.edu.ku.ulgen.service;

import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tr.edu.ku.ulgen.entity.AffectedData;
import tr.edu.ku.ulgen.repository.AffectedDataRepository;
import tr.edu.ku.ulgen.response.AffectedCitiesSetResponse;
import tr.edu.ku.ulgen.response.ConfigPropertyResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * A service class responsible for managing affected cities and their data.
 * It provides methods to get affected cities, and set the affected status of a list of cities.
 *
 * @author Kaan Turkmen
 */
@Service
@AllArgsConstructor
@Slf4j
public class AffectedDataService {

    private final AffectedDataRepository affectedDataRepository;

    /**
     * Retrieves a list of affected cities.
     *
     * @return a list of affected city names.
     */
    public List<String> getAffectedCities() {
        List<AffectedData> affectedDataList;

        try {
            affectedDataList = affectedDataRepository.findAll();
        } catch (Exception e) {
            log.error("Could not run findAll on the database.");
            log.error("Database is not reachable, {}", e.getMessage());
            return Collections.emptyList();
        }

        return affectedDataList.stream().filter(AffectedData::getAffected).map(AffectedData::getCityName).collect(Collectors.toList());
    }

    /**
     * Sets the affected status for a list of city names.
     *
     * @param cityNames a list of city names for which the affected status should be set.
     * @param value     the affected status value to set for the given city names.
     *
     * @return a {@link ResponseEntity} contains {@link AffectedCitiesSetResponse} with the result of the operation.
     */
    public ResponseEntity<AffectedCitiesSetResponse> setAffectedCities(List<String> cityNames, Boolean value) {
        for (String cityName : cityNames) {
            Optional<AffectedData> affectedDataOptional;

            try {
                affectedDataOptional = affectedDataRepository.findById(cityName);
            } catch (PersistenceException e) {
                log.error("Could not run findById on the database.");
                log.error("Database is not reachable, {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(AffectedCitiesSetResponse.builder().result("SERVICE_UNAVAILABLE").build());
            }

            if (affectedDataOptional.isPresent()) {
                AffectedData affectedData = affectedDataOptional.get();
                affectedData.setAffected(value);

                try {
                    affectedDataRepository.save(affectedData);
                } catch (PersistenceException e) {
                    log.error("Could not write affectedData to the database.");
                    log.error("Database is not reachable, {}", e.getMessage());
                    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(AffectedCitiesSetResponse.builder().result("SERVICE_UNAVAILABLE").build());
                }
            } else {
                AffectedData newAffectedData = new AffectedData();
                newAffectedData.setCityName(cityName);
                newAffectedData.setAffected(value);

                try {
                    affectedDataRepository.save(newAffectedData);
                } catch (PersistenceException e) {
                    log.error("Could not write newAffectedData to the database.");
                    log.error("Database is not reachable, {}", e.getMessage());
                    return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(AffectedCitiesSetResponse.builder().result("SERVICE_UNAVAILABLE").build());
                }
            }
        }

        return ResponseEntity.ok().body(AffectedCitiesSetResponse.builder().result("SUCCESS").build());
    }
}

