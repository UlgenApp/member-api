package tr.edu.ku.ulgen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UlgenApplication {

    public static void main(String[] args) {
        SpringApplication.run(UlgenApplication.class, args);
    }

}
