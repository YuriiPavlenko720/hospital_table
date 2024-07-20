package lemon.hospitaltable.table.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RunCheckController {

    @GetMapping("/")
    public String test() {
        return "The hospital table is running!";
    }

    @GetMapping("/auth_test")
    public String authTest() {
        return "The hospital table is running! This is an endpoint accessible by only authenticated users.";
    }
}
