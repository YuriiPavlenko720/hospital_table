package lemon.hospitaltable.table.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lemon.hospitaltable.table.configurations.OpenAiConfig;
import lemon.hospitaltable.table.configurations.PromptConfig;
import lemon.hospitaltable.table.objects.Doctor;
import lemon.hospitaltable.table.objects.Patient;
import lemon.hospitaltable.table.objects.ai.ChatCompletionRequest;
import lemon.hospitaltable.table.objects.ai.ChatCompletionResponse;
import lemon.hospitaltable.table.objects.ai.Message;
import lemon.hospitaltable.table.repositories.DoctorsRepositoryInterface;
import lemon.hospitaltable.table.repositories.PatientsRepositoryInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@AllArgsConstructor
@Service
public class OpenAiIntegrationService {

    private final OpenAiConfig openAiConfig;
    private final PromptConfig promptConfig;
    private final PatientsRepositoryInterface patientsRepository;
    private final DoctorsRepositoryInterface doctorsRepository;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;


    public String getRecommendation(Long patientId, String symptoms) {

        Patient patient = patientsRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient ID " + patientId + " not found."));

        List<Doctor> doctors = (List<Doctor>) doctorsRepository.findAll();

        String prompt = promptConfig.getRecommendationRequestMessage()
                .replace("${patient}", patient.toString())
                .replace("${symptoms}", symptoms)
                .replace("${doctors}", doctors.toString());

        try {
            ChatCompletionRequest requestPayload = new ChatCompletionRequest(
                    "gpt-4o",
                    List.of(new Message("user", prompt))
            );

            String requestBody = objectMapper.writeValueAsString(requestPayload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://api.openai.com/v1/chat/completions"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + openAiConfig.getApiKey())
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            ChatCompletionResponse completionResponse = objectMapper.readValue(
                    response.body(),
                    ChatCompletionResponse.class
            );
            return completionResponse.choices().getFirst().message().content();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
