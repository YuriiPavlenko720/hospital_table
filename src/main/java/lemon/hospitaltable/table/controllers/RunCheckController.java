package lemon.hospitaltable.table.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RunCheckController {

    @GetMapping("/api/check")
    public String runCheckStatus() {
        return "App is running!";
    }
}

