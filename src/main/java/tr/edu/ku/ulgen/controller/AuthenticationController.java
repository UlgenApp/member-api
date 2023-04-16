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

/**
 * REST controller for handling user authentication and registration.
 * This class exposes API endpoints for registering a new user and authenticating an existing user.
 *
 * @author Kaan Turkmen
 */
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Registers a new user with the provided registration details.
     *
     * @param registerDto the {@link RegisterDto} object containing the user's registration details.
     * @return a {@link ResponseEntity} containing the {@link AuthenticationResponse} if registration is successful,
     * or a bad request status if registration fails.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {

        log.info("Register request is received: {}", registerDto.getEmail());
        AuthenticationResponse authenticationResponse = authenticationService.register(registerDto);

        if (authenticationResponse == null) {
            log.error("Register failed for: {}", registerDto.getEmail());
            return ResponseEntity.badRequest().build();
        }

        log.info("Register successful for: {}", registerDto.getEmail());
        return ResponseEntity.ok(authenticationResponse);
    }

    /**
     * Authenticates a user with the provided email and password.
     *
     * @param authenticationDto the {@link AuthenticationDto} object containing the user's email and password.
     * @return a {@link ResponseEntity} containing the {@link AuthenticationResponse} with the authentication result.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationDto authenticationDto) {

        log.info("Login request is received: {}", authenticationDto.getEmail());
        return ResponseEntity.ok(authenticationService.authenticate(authenticationDto));
    }
}
