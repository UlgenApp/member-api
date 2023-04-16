package tr.edu.ku.ulgen.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * A service class is responsible for sending emails using the JavaMailSender.
 * It is a service that can be used to send simple text emails with a specified recipient, subject, and body.
 *
 * @author Kaan Turkmen
 */

@Slf4j
@AllArgsConstructor
@Service
public class EmailSenderService {

    private JavaMailSender javaMailSender;

    @Value("${ULGEN_EMAIL}")
    private String from;

    /**
     * Sends an email with the specified recipient, subject, and body.
     *
     * @param to      the email address of the recipient
     * @param subject the subject of the email
     * @param body    the body of the email
     *
     * @throws MailParseException if the email cannot be parsed
     * @throws MailAuthenticationException if the sender cannot be authenticated
     * @throws MailSendException if the email cannot be sent
     */
    public void sendEmail(String to, String subject, String body) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);

        try {
            javaMailSender.send(simpleMailMessage);
        } catch (MailParseException mailParseException) {
            log.error("Encountered with MailParseException while sending mail to {}", to);
        } catch (MailAuthenticationException mailAuthenticationException) {
            log.error("Encountered with MailAuthenticationException while sending mail to {}", to);
        } catch (MailSendException mailSendException) {
            log.error("Encountered with MailSendException while sending mail to {}", to);
        }

    }
}
