package tr.edu.ku.ulgen.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(30000, 30000, 2);
    }
}
