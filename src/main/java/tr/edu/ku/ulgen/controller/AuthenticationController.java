package tr.edu.ku.ulgen.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.edu.ku.ulgen.dto.AuthenticationDto;
import tr.edu.ku.ulgen.dto.RegisterDto;
import tr.edu.ku.ulgen.response.AuthenticationResponse;
import tr.edu.ku.ulgen.service.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {

        log.info("Register request is received: {}", registerDto);
        AuthenticationResponse authenticationResponse = service.register(registerDto);

        if (authenticationResponse == null) {
            log.error("Register failed for: {}", registerDto);
            return ResponseEntity.badRequest().build();
        }

        log.info("Register successful for: {}", registerDto);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationDto authenticationDto) {

        log.info("Login request is received: {}", authenticationDto);
        return ResponseEntity.ok(service.authenticate(authenticationDto));
    }
}
