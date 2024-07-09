package lemon.hospitaltable.table.services;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import lemon.hospitaltable.table.configurations.PromptConfig;
import lemon.hospitaltable.table.objects.Doctor;
import lemon.hospitaltable.table.objects.Patient;
import lemon.hospitaltable.table.repositories.DoctorsRepositoryInterface;
import lemon.hospitaltable.table.repositories.PatientsRepositoryInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Service
public class AIService {

    private PatientsRepositoryInterface patientsRepository;
    private DoctorsRepositoryInterface doctorsRepository;
    private OpenAiService openAiService;
    private PromptConfig promptConfig;

    public String recommendDoctor(Long patientId, String symptoms) {
        Patient patient = patientsRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient ID " + patientId + " not found."));

        List<Doctor> doctors = (List<Doctor>) doctorsRepository.findAll();

        String prompt = promptConfig.getRecommendationRequestMessage()
                .replace("${patient}", patient.toString())
                .replace("${symptoms}", symptoms)
                .replace("${doctors}", doctors.toString());

        //  Symptoms analysis and selection of a doctor
        ChatMessage message = new ChatMessage("user", prompt);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(Collections.singletonList(message))
                .maxTokens(2000)
                .build();

        ChatCompletionResult chatCompletionResult = openAiService.createChatCompletion(chatCompletionRequest);

        return chatCompletionResult.getChoices().getFirst().getMessage().getContent();
    }
}
