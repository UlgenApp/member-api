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

/**
 * A service class provides service to interact with the UserRepository
 * to manage user data, specifically updating additional information.
 *
 * @author Kaan Turkmen
 */
@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class UserDataService {
    private final UserRepository userRepository;

    /**
     * Updates the additional information of the authenticated user.
     *
     * @param additionalInfoDto An AdditionalInfoDto object containing the new additional information.
     * @return Boolean value indicating whether the update was successful (true) or not (false).
     */
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
