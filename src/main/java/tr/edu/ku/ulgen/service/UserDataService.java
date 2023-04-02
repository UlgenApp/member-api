package tr.edu.ku.ulgen.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.edu.ku.ulgen.dto.AdditionalInfoDto;
import tr.edu.ku.ulgen.entity.User;
import tr.edu.ku.ulgen.repository.UserRepository;
import tr.edu.ku.ulgen.util.AuthenticatedUser;

@Service
@AllArgsConstructor
@Transactional
public class UserDataService {
    private UserRepository userRepository;

    public Boolean updateAdditionalInfo(AdditionalInfoDto additionalInfoDto) {
        User authenticatedUser = new AuthenticatedUser(userRepository).getAuthenticatedUser();
        Integer numberOfRowsUpdated = userRepository.updateAdditionalInfo(additionalInfoDto.getAdditionalInfo(), authenticatedUser.getId());
        return numberOfRowsUpdated == 1;
    }
}
