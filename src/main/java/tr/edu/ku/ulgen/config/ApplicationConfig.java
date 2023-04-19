package tr.edu.ku.ulgen.config;

import jakarta.persistence.PersistenceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tr.edu.ku.ulgen.repository.UserRepository;

import java.util.Properties;

/**
 * Configuration class for the application's beans and authentication.
 * This class sets up beans for {@link UserDetailsService}, {@link AuthenticationProvider},
 * {@link AuthenticationManager}, and {@link PasswordEncoder}.
 *
 * @author Kaan Turkmen
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final UserRepository userRepository;

    @Value("${ULGEN_EMAIL}")
    private String ulgenEmail;

    @Value("${ULGEN_EMAIL_PASSWORD}")
    private String ulgenEmailPassword;

    /**
     * Provides a custom {@link UserDetailsService} implementation that uses the {@link UserRepository}
     * to load user data by email.
     *
     * @return a {@link UserDetailsService} instance.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        try {
            return username -> userRepository.findByEmail(username).orElse(null);
        } catch (PersistenceException e) {
            log.error("Could not execute find by email on the database.");
            log.error("Database is not reachable, {}", e.getMessage());

            return null;
        }
    }

    /**
     * Configures an {@link AuthenticationProvider} using {@link DaoAuthenticationProvider}
     * with the custom {@link UserDetailsService} and a {@link PasswordEncoder}.
     *
     * @return an {@link AuthenticationProvider} instance.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Exposes the {@link AuthenticationManager} bean for configuring authentication.
     *
     * @param authenticationConfiguration the {@link AuthenticationConfiguration} instance.
     * @return an {@link AuthenticationManager} instance.
     * @throws Exception if there's an issue with the authentication configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Configures the {@link PasswordEncoder} bean to use {@link BCryptPasswordEncoder} for password hashing.
     *
     * @return a {@link PasswordEncoder} instance.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates a new JavaMailSender instance with the necessary configurations.
     * Sets the host, port, username, password, and other properties required for sending emails.
     *
     * @return a configured JavaMailSender instance for sending emails.
     */
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(ulgenEmail);
        mailSender.setPassword(ulgenEmailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return mailSender;
    }
}
