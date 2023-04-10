package tr.edu.ku.ulgen.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.edu.ku.ulgen.entity.UlgenConfig;
import tr.edu.ku.ulgen.repository.UlgenConfigRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UlgenConfigService {
    private UlgenConfigRepository configRepository;

    public Boolean isAlerted() {
        Boolean configValue = getConfigPropertyValue("alerted");
        return configValue != null ? configValue : Boolean.FALSE;
    }


    public Boolean getConfigPropertyValue(String configPropertyName) {
        Optional<UlgenConfig> configOptional = configRepository.findById(configPropertyName);
        return configOptional.map(UlgenConfig::getConfigPropertyValue).orElse(null);
    }

    public void setConfigPropertyValue(String configPropertyName, Boolean value) {
        Optional<UlgenConfig> configOptional = configRepository.findById(configPropertyName);
        if (configOptional.isPresent()) {
            log.info("Config is found, updating.");
            UlgenConfig config = configOptional.get();
            config.setConfigPropertyValue(value);
            configRepository.save(config);
        } else {
            log.info("Config is not found, creating.");
            UlgenConfig ulgenConfig = UlgenConfig.builder()
                    .configPropertyName(configPropertyName)
                    .configPropertyValue(value)
                    .build();

            configRepository.save(ulgenConfig);
        }
    }
}
