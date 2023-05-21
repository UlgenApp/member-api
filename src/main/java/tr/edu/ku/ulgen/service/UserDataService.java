package tr.edu.ku.ulgen.service;

import jakarta.persistence.PersistenceException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.edu.ku.ulgen.dto.AdditionalInfoDto;
import tr.edu.ku.ulgen.entity.UlgenData;
import tr.edu.ku.ulgen.entity.User;
import tr.edu.ku.ulgen.repository.UlgenDataRepository;
import tr.edu.ku.ulgen.repository.UserRepository;
import tr.edu.ku.ulgen.response.UserResponse;
import tr.edu.ku.ulgen.util.AuthenticatedUser;

import java.util.List;
import java.util.Optional;

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
    private final UlgenDataRepository ulgenDataRepository;
    private final AffectedDataService affectedDataService;

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

    /**
     * Gets related data with the user while not leaking the database schema.
     *
     * @return a {@link ResponseEntity<UserResponse>} containing the result of the profile.
     */
    public ResponseEntity<UserResponse> getUserProfile() {
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(userRepository);

        User u = authenticatedUser.getAuthenticatedUser();

        if (u == null) {
            return ResponseEntity.badRequest().body(UserResponse.builder().build());
        }

        return ResponseEntity.ok().body(
                UserResponse.builder()
                        .firstName(u.getFirstName())
                        .lastName(u.getLastName())
                        .email(u.getEmail())
                        .additionalInfo(u.getAdditionalInfo())
                        .build()
        );
    }

    public ResponseEntity<?> safe() {
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(userRepository);

        User u = authenticatedUser.getAuthenticatedUser();

        if (u == null) {
            return ResponseEntity.badRequest().body(UserResponse.builder().build());
        }

        Optional<UlgenData> ulgenDataOptional;

        try {
            ulgenDataOptional = ulgenDataRepository.findByUserId(u.getId());
        } catch (PersistenceException e) {
            log.error("Could not deleted user from Ulgen Data.");
            log.error("Database is not reachable, {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service is currently unavailable");
        }

        if (ulgenDataOptional.isEmpty()) {
            return ResponseEntity.badRequest().body(UserResponse.builder().build());
        }

        UlgenData ulgenData = ulgenDataOptional.get();

        String userCity = ulgenData.getUserCity();

        List<String> affectedCities = affectedDataService.getAffectedCities();

        if (!affectedCities.contains(userCity)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("You are not in the danger zone.");
        }

        try {
            ulgenDataRepository.deleteByUserId(u.getId());
        } catch (PersistenceException e) {
            log.error("Could not deleted user from Ulgen Data.");
            log.error("Database is not reachable, {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service is currently unavailable");
        }

        return ResponseEntity.ok().build();
    }
}
