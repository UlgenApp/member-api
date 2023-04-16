package tr.edu.ku.ulgen.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tr.edu.ku.ulgen.repository.UserRepository;

/**
 * Configuration class for the application's beans and authentication.
 * This class sets up beans for {@link UserDetailsService}, {@link AuthenticationProvider},
 * {@link AuthenticationManager}, and {@link PasswordEncoder}.
 *
 * @author Kaan Turkmen
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final UserRepository userRepository;

    /**
     * Provides a custom {@link UserDetailsService} implementation that uses the {@link UserRepository}
     * to load user data by email.
     *
     * @return a {@link UserDetailsService} instance.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
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

}
