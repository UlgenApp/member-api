package tr.edu.ku.ulgen.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign Client Configuration to Handle Spring Security.
 *
 * @author Kaan Turkmen
 */
@Configuration
public class FeignClientConfiguration {

    /**
     * Creates Feign Retryer bean with custom configuration.
     * This bean configures the retry behavior for Feign clients.
     *
     * @return a Retryer instance with a 30000ms period, 30000ms max period, and a maximum of 2 attempts.
     */
    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(30000, 30000, 2);
    }
}
