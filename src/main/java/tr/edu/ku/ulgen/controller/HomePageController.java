package tr.edu.ku.ulgen.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.edu.ku.ulgen.dto.ForgotPasswordDto;
import tr.edu.ku.ulgen.dto.ResendEmailVerificationDto;
import tr.edu.ku.ulgen.dto.ResetPasswordDto;
import tr.edu.ku.ulgen.response.ForgotPasswordResponse;
import tr.edu.ku.ulgen.response.ResetPasswordResponse;
import tr.edu.ku.ulgen.response.VerifyEmailResponse;
import tr.edu.ku.ulgen.service.AuthenticationService;

/**
 * REST controller for handling the home page request.
 * This class exposes an API endpoint for the root URL ("/") of the application.
 *
 * @author Kaan Turkmen
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class HomePageController {
    private final AuthenticationService authenticationService;

    /**
     * Returns a simple message when the root URL ("/") is accessed.
     *
     * @return a {@link String} containing the message to be displayed on the home page.
     */
    @GetMapping("/")
    public String hello() {
        return "Psst! You found us. Unfortunately this website serves as a REST API, thus, there is nothing interesting in here :(";
    }

    /**
     * Verifies the user's email by the given token.
     *
     * @param token a {@link String} containing the email verification token.
     * @return a {@link String} containing the result of the email verification process.
     */
    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String token) {
        ResponseEntity<VerifyEmailResponse> result = authenticationService.verifyEmail(token);

        if (result.getStatusCode().equals(HttpStatus.OK)) {
            return "Email Adresin Başarıyla Doğrulandı, Ülgeni kullanmaya başlayabilirsin!";
        } else {
            return "Email Adresini Doğrularken Bir Hata Oluştu, Lütfen Tekrar Dene!";
        }
    }

    /**
     * Resends the email verification message to the specified email address.
     *
     * @param resendEmailVerificationDto A {@link ResendEmailVerificationDto} containing the email address.
     * @return A {@link ResponseEntity<VerifyEmailResponse>} containing the result of the email resend process.
     */
    @PostMapping("/resend-email-verification")
    public ResponseEntity<VerifyEmailResponse> resendEmailVerification(@RequestBody ResendEmailVerificationDto resendEmailVerificationDto) {
        return authenticationService.resendEmailVerification(resendEmailVerificationDto.getEmail());
    }

    /**
     * Initiates the forgot password process for the specified email address.
     *
     * @param forgotPasswordDto A {@link ForgotPasswordDto} containing the email address.
     * @return A {@link ResponseEntity<ForgotPasswordResponse>} containing the result of the forgot password process.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        return authenticationService.forgotPassword(forgotPasswordDto.getEmail());
    }

    /**
     * Resets the user's password using the provided token and new password.
     *
     * @param resetPasswordDto A {@link ResetPasswordDto} containing the password reset token and the new password.
     * @return A {@link ResponseEntity<ResetPasswordResponse>} containing the result of the password reset process.
     */
    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        return authenticationService.resetPassword(resetPasswordDto.getToken(), resetPasswordDto.getNewPassword());
    }
}
