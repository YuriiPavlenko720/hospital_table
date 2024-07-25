package lemon.hospitaltable.table.controllers;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RunCheckController {

    @GetMapping("/")
    public String home() {
        return "App is running! Welcome to the home page!";
    }

    @GetMapping("/secured")
    public String secured() {
        return "You have accessed a secured page!";
    }

    @GetMapping("/admin")
    @Secured("ROLE_ADMIN")
    public String admin() {
        return "You have accessed the admin page!";
    }
}
