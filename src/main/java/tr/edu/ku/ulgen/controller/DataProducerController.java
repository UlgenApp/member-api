package tr.edu.ku.ulgen.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.edu.ku.ulgen.client.ProducerClient;
import tr.edu.ku.ulgen.dto.ProducerDataDto;
import tr.edu.ku.ulgen.dto.ProducerDto;
import tr.edu.ku.ulgen.repository.UserRepository;
import tr.edu.ku.ulgen.util.AuthenticatedUser;

@RestController
@RequestMapping("/api/v1/producer")
@AllArgsConstructor
public class DataProducerController {
    private ProducerClient producerClient;
    private UserRepository userRepository;

    @PostMapping("/produce")
    public ResponseEntity<?> produce(@RequestBody ProducerDataDto request) {
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(userRepository);
        return ResponseEntity.ok(
                producerClient.produceData(ProducerDto.builder()
                        .userId(authenticatedUser.getAuthenticatedUser().getId())
                        .location(request.getLocation())
                        .activeUser(request.getActiveUser())
                        .userCity(request.getUserCity())
                        .build())
        );
    }
}
