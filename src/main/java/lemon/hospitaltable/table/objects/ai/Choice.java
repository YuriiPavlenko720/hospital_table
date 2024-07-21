package lemon.hospitaltable.table.objects.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Choice (int index, Message message, String finish_reason) {
}
