package tr.edu.ku.ulgen.service;

import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tr.edu.ku.ulgen.entity.AffectedData;
import tr.edu.ku.ulgen.repository.AffectedDataRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AffectedDataService {

    private final AffectedDataRepository affectedDataRepository;

    public List<String> getAffectedCities() {
        List<AffectedData> affectedDataList = new ArrayList<>();

        try {
            affectedDataList = affectedDataRepository.findAll();
        } catch (Exception e) {
            log.error("Could not run findAll on the database.");
            log.error("Database is not reachable, {}", e.getMessage());
        }

        return affectedDataList.stream()
                .filter(AffectedData::getAffected)
                .map(AffectedData::getCityName)
                .collect(Collectors.toList());
    }

    public void setAffectedCities(List<String> cityNames, Boolean value) {
        for (String cityName : cityNames) {
            Optional<AffectedData> affectedDataOptional = null;

            try {
                affectedDataOptional = affectedDataRepository.findById(cityName);
            } catch (PersistenceException e) {
                log.error("Could not run findById on the database.");
                log.error("Database is not reachable, {}", e.getMessage());
            }

            if (affectedDataOptional == null) {
                return;
            }

            if (affectedDataOptional.isPresent()) {
                AffectedData affectedData = affectedDataOptional.get();
                affectedData.setAffected(value);

                try {
                    affectedDataRepository.save(affectedData);
                } catch (PersistenceException e) {
                    log.error("Could not write affectedData to the database.");
                    log.error("Database is not reachable, {}", e.getMessage());
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
                }
            }
        }
    }
}

