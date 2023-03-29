package tr.edu.ku.ulgen.controller;

import lombok.RequiredArgsConstructor;
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
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto request) {
        AuthenticationResponse authenticationResponse = service.register(request);

        if (authenticationResponse == null) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationDto request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
