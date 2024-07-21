package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.services.OpenAiIntegrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final OpenAiIntegrationService openAiIntegrationService;

    @GetMapping("/recommend_doctor")
    public String getRecommendation(@RequestParam Long patientId, @RequestParam String symptoms) {
        return openAiIntegrationService.getRecommendation(patientId, symptoms);
    }
}
