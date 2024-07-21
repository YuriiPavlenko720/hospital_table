package lemon.hospitaltable.table.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RunCheckController {

    private static final Logger logger = LoggerFactory.getLogger(RunCheckController.class);

    @GetMapping("/")
    public String home() {

        logger.info("Accessing / endpoint");

        return "The hospital table is running!";
    }

    @GetMapping("/privates")
    public String privates() {

        logger.info("Accessing /privates endpoint");

        return "The hospital table is running! This is an endpoint accessible by only authenticated users.";
    }

    @GetMapping("/secrets")
    @PreAuthorize("hasRole('ADMIN')")
    public String secrets() {

        logger.info("Accessing /secrets endpoint");

        return "The hospital table is running! This is an endpoint accessible by only ADMINS.";
    }
}
