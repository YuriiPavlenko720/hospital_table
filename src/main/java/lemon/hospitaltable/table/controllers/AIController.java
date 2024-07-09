package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.services.AIService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final AIService aiService;

    @PostMapping("/recommend_doctor")
    public String recommendDoctor(@RequestParam Long patientId, @RequestParam String symptoms) {
        return aiService.recommendDoctor(patientId, symptoms);
    }
}
