package tr.edu.ku.ulgen.service;


import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tr.edu.ku.ulgen.entity.UlgenConfig;
import tr.edu.ku.ulgen.repository.UlgenConfigRepository;
import tr.edu.ku.ulgen.response.ConfigPropertyResponse;

import java.util.Optional;

/**
 * A service class provides service to interact with the
 * UlgenConfigRepository to manage the configuration properties.
 *
 * @author Kaan Turkmen
 */
@Service
@AllArgsConstructor
@Slf4j
public class UlgenConfigService {
    private final UlgenConfigRepository configRepository;

    /**
     * Checks if the alerted property is set in the configuration.
     *
     * @return Boolean value of the alerted property, or null if not found or an error occurs.
     */
    public Boolean isAlerted() {
        return getConfigPropertyValue("alerted");
    }

    /**
     * Retrieves the Boolean value of the given configuration property name.
     *
     * @param configPropertyName The name of the configuration property to retrieve.
     * @return Boolean value of the configuration property, or null if not found or an error occurs.
     */
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

    /**
     * Sets the Boolean value for the given configuration property name.
     * If the configuration property exists, it updates the value.
     * If the configuration property does not exist, it creates a new one.
     *
     * @param configPropertyName The name of the configuration property to set.
     * @param value              The Boolean value to set for the configuration property.
     * @return a {@link ResponseEntity} containing a {@link ConfigPropertyResponse} with the result of the operation.
     */
    public ResponseEntity<ConfigPropertyResponse> setConfigPropertyValue(String configPropertyName, Boolean value) {
        Optional<UlgenConfig> configOptional;

        try {
            configOptional = configRepository.findById(configPropertyName);
        } catch (PersistenceException e) {
            log.error("Could not called findById on the database.");
            log.error("Database is not reachable, {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ConfigPropertyResponse.builder().result("SERVICE_UNAVAILABLE").build());
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
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ConfigPropertyResponse.builder().result("SERVICE_UNAVAILABLE").build());
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
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ConfigPropertyResponse.builder().result("SERVICE_UNAVAILABLE").build());
            }
        }

        return ResponseEntity.ok().body(ConfigPropertyResponse.builder().result("SUCCESS").build());
    }
}
