package tr.edu.ku.ulgen.service;

import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.edu.ku.ulgen.dto.AdditionalInfoDto;
import tr.edu.ku.ulgen.entity.User;
import tr.edu.ku.ulgen.repository.UserRepository;
import tr.edu.ku.ulgen.util.AuthenticatedUser;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class UserDataService {
    private UserRepository userRepository;

    public Boolean updateAdditionalInfo(AdditionalInfoDto additionalInfoDto) {
        User authenticatedUser = new AuthenticatedUser(userRepository).getAuthenticatedUser();


        Integer numberOfRowsUpdated = 0;
        try {
            numberOfRowsUpdated = userRepository.updateAdditionalInfo(additionalInfoDto.getAdditionalInfo(), authenticatedUser.getId());
        } catch (PersistenceException e) {
            log.error("Could not update additional info on the database.");
            log.error("Database is not reachable, {}", e.getMessage());
        }

        log.info("Affected number of rows: {} - Should be exactly one.", numberOfRowsUpdated);
        return numberOfRowsUpdated == 1;
    }
}
