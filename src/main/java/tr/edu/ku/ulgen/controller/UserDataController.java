package tr.edu.ku.ulgen.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.edu.ku.ulgen.dto.AdditionalInfoDto;
import tr.edu.ku.ulgen.service.UserDataService;

/**
 * REST controller for handling user data updates.
 * This class exposes an API endpoint for updating the additional information of a user.
 *
 * @author Kaan Turkmen
 */
@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
@Slf4j
public class UserDataController {

    private final UserDataService userDataService;

    /**
     * Updates the additional information of a user with the provided {@link AdditionalInfoDto}.
     *
     * @param additionalInfoDto the {@link AdditionalInfoDto} object containing the additional information to be updated.
     * @return a {@link ResponseEntity} containing the status of the update operation.
     */
    @PostMapping("/update/additional-info")
    public ResponseEntity<?> updateAdditionalInfo(@RequestBody AdditionalInfoDto additionalInfoDto) {
        log.info("Update Additional Info request is received: {}", additionalInfoDto);

        Boolean isUpdated = userDataService.updateAdditionalInfo(additionalInfoDto);
        return isUpdated ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
