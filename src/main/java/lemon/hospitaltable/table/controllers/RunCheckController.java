package lemon.hospitaltable.table.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RunCheckController {

    private static final Logger logger = LoggerFactory.getLogger(RunCheckController.class);

    @GetMapping("/")
    public String home() {
        return "App is running! Welcome to the home page!";
    }

    @GetMapping("/secured")
    public String secured() {
        logger.info("Accessing secured endpoint");
        return "You have accessed a secured page!";
    }

    @GetMapping("/admin")
    @Secured("ROLE_ADMIN")
    public String admin() {
        logger.info("Accessing admin endpoint");
        return "You have accessed the admin page!";
    }
}
