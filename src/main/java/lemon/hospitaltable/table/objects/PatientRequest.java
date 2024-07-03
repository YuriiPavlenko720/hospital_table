package lemon.hospitaltable.table.objects;

import java.time.LocalDate;

public record PatientRequest(
        String name,
        LocalDate birth,
        String sex,
        String phone,
        String interests,
        String address,
        String email,
        String status,
        String notation
) {
}
