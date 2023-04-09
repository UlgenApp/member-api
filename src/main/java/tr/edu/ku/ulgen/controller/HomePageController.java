package tr.edu.ku.ulgen.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomePageController {

    @GetMapping("/")
    public String hello() {
        return "Psst! You found us. Unfortunately this website serves as a REST API," +
                " thus, there is nothing interesting in here :(";
    }
}
