package lemon.hospitaltable.table.configurations;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class OpenAiConfig {

    @Value("${openai.api.key}")
    private String apiKey;

}
