package tr.edu.ku.ulgen.service;


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
    private UlgenConfigRepository configRepository;

    public Boolean isAlerted() {
        return Boolean.FALSE.equals(getConfigPropertyValue("alerted"));
    }


    public Boolean getConfigPropertyValue(String configPropertyName) {
        Optional<UlgenConfig> configOptional = configRepository.findById(configPropertyName);
        return configOptional.map(UlgenConfig::getConfigPropertyValue).orElse(null);
    }

    public void setConfigPropertyValue(String configPropertyName, Boolean value) {
        Optional<UlgenConfig> configOptional = configRepository.findById(configPropertyName);
        if (configOptional.isPresent()) {
            UlgenConfig config = configOptional.get();
            config.setConfigPropertyValue(value);
            configRepository.save(config);
        } else {
            UlgenConfig ulgenConfig = UlgenConfig.builder()
                    .configPropertyName(configPropertyName)
                    .configPropertyValue(value)
                    .build();

            configRepository.save(ulgenConfig);
        }
    }
}
