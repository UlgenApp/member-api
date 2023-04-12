package tr.edu.ku.ulgen.controller;

import feign.FeignException;
import jakarta.transaction.TransactionalException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.edu.ku.ulgen.client.ProducerClient;
import tr.edu.ku.ulgen.dto.ProducerDataDto;
import tr.edu.ku.ulgen.dto.ProducerDto;
import tr.edu.ku.ulgen.repository.MACAddressRepository;
import tr.edu.ku.ulgen.repository.UserRepository;
import tr.edu.ku.ulgen.service.MACAddressService;
import tr.edu.ku.ulgen.util.AuthenticatedUser;

import java.net.UnknownHostException;

@RestController
@RequestMapping("/api/v1/producer")
@AllArgsConstructor
@Slf4j
public class DataProducerController {
    private ProducerClient producerClient;
    private UserRepository userRepository;
    private MACAddressService macAddressService;

    @PostMapping("/produce")
    public ResponseEntity<?> produce(@RequestBody ProducerDataDto producerDataDto) {

        log.info("Produce request is received: {}", producerDataDto);
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(userRepository);

        try {
            return ResponseEntity.ok(
                    producerClient.produceData(ProducerDto.builder()
                            .userId(authenticatedUser.getAuthenticatedUser().getId())
                            .location(producerDataDto.getLocation())
                            .activeUser(macAddressService.countMatchingMACAddresses(producerDataDto.getMacAddresses()))
                            .userCity(producerDataDto.getUserCity())
                            .build())
            );
        } catch (FeignException e) {
            log.error("FeignException occured, producer-api is down: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Service is currently unavailable");
        } catch (TransactionalException te) {
            log.error("Could not called findByToken on the database.");
            log.error("Database is not reachable, {}", te.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
