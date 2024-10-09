package lemon.hospitaltable.table.objects;

import java.time.LocalDate;

public record TreatmentRequest(
        Long patientId,
        Integer doctorId,
        Integer wardId,
        LocalDate dateIn,
        LocalDate dateOut,
        String diagnosis,
        String notation
) {
}
