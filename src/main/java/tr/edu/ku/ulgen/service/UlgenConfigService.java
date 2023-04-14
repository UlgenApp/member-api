package tr.edu.ku.ulgen.service;


import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tr.edu.ku.ulgen.entity.UlgenConfig;
import tr.edu.ku.ulgen.repository.UlgenConfigRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UlgenConfigService {
    private final UlgenConfigRepository configRepository;

    public Boolean isAlerted() {
        return getConfigPropertyValue("alerted");
    }


    public Boolean getConfigPropertyValue(String configPropertyName) {
        Optional<UlgenConfig> configOptional;

        try {
            configOptional = configRepository.findById(configPropertyName);
        } catch (PersistenceException e) {
            log.error("Could not called findById on the database.");
            log.error("Database is not reachable, {}", e.getMessage());
            return null;
        }

        return configOptional.map(UlgenConfig::getConfigPropertyValue).orElse(null);
    }

    public void setConfigPropertyValue(String configPropertyName, Boolean value) {
        Optional<UlgenConfig> configOptional;

        try {
            configOptional = configRepository.findById(configPropertyName);
        } catch (PersistenceException e) {
            log.error("Could not called findById on the database.");
            log.error("Database is not reachable, {}", e.getMessage());
            return;
        }


        if (configOptional.isPresent()) {
            log.info("Config is found, updating.");
            UlgenConfig config = configOptional.get();
            config.setConfigPropertyValue(value);

            try {
                configRepository.save(config);
            } catch (PersistenceException e) {
                log.error("Could not saved config to the database.");
                log.error("Database is not reachable, {}", e.getMessage());
            }
        } else {
            log.info("Config is not found, creating.");
            UlgenConfig ulgenConfig = UlgenConfig.builder()
                    .configPropertyName(configPropertyName)
                    .configPropertyValue(value)
                    .build();

            try {
                configRepository.save(ulgenConfig);
            } catch (PersistenceException e) {
                log.error("Could not saved config to the database.");
                log.error("Database is not reachable, {}", e.getMessage());
            }
        }
    }
}
