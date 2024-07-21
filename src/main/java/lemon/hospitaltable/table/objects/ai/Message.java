package lemon.hospitaltable.table.objects.ai;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Message (
        String role,
        String content
){
}
