package lemon.hospitaltable.table.objects;

import java.time.LocalDate;

public record DoctorRequest(
        String name,
        LocalDate birth,
        String sex,
        String address,
        String phone,
        String interests,
        String position,
        Integer departmentId,
        String email
) {
}
