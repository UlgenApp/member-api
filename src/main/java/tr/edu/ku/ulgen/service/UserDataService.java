package tr.edu.ku.ulgen.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.edu.ku.ulgen.dto.AdditionalInfoDto;
import tr.edu.ku.ulgen.entity.User;
import tr.edu.ku.ulgen.repository.UserRepository;
import tr.edu.ku.ulgen.util.AuthenticatedUser;

@Service
@RequiredArgsConstructor
public class UserDataService {
    private UserRepository userRepository;

    public Boolean updateAdditionalInfo(AdditionalInfoDto additionalInfoDto) {
        User authenticatedUser = new AuthenticatedUser(userRepository).getAuthenticatedUser();
        return userRepository.updateAdditionalInfo(authenticatedUser.getUsername(), additionalInfoDto.getAdditionalInfo());
    }
}
