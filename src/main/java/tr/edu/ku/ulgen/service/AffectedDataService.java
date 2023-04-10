package tr.edu.ku.ulgen.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tr.edu.ku.ulgen.entity.AffectedData;
import tr.edu.ku.ulgen.repository.AffectedDataRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class AffectedDataService {

    private AffectedDataRepository affectedDataRepository;

    public List<String> getAffectedCities() {
        List<AffectedData> affectedDataList = affectedDataRepository.findAll();

        return affectedDataList.stream()
                .filter(AffectedData::getAffected)
                .map(AffectedData::getCityName)
                .collect(Collectors.toList());
    }

    public void setAffectedCities(List<String> cityNames, Boolean value) {
        for (String cityName : cityNames) {
            Optional<AffectedData> affectedDataOptional = affectedDataRepository.findById(cityName);
            if (affectedDataOptional.isPresent()) {
                AffectedData affectedData = affectedDataOptional.get();
                affectedData.setAffected(value);
                affectedDataRepository.save(affectedData);
            } else {
                AffectedData newAffectedData = new AffectedData();
                newAffectedData.setCityName(cityName);
                newAffectedData.setAffected(value);
                affectedDataRepository.save(newAffectedData);
            }
        }
    }
}

