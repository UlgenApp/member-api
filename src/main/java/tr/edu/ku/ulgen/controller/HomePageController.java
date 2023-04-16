package tr.edu.ku.ulgen.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for handling the home page request.
 * This class exposes an API endpoint for the root URL ("/") of the application.
 *
 * @author Kaan Turkmen
 */
@RestController
public class HomePageController {

    /**
     * Returns a simple message when the root URL ("/") is accessed.
     *
     * @return a {@link String} containing the message to be displayed on the home page.
     */
    @GetMapping("/")
    public String hello() {
        return "Psst! You found us. Unfortunately this website serves as a REST API, thus, there is nothing interesting in here :(";
    }
}
