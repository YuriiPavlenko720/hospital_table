package lemon.hospitaltable.table.configurations;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "prompt")
@Data
public class PromptConfig {
    private String recommendationRequestMessage;
}
