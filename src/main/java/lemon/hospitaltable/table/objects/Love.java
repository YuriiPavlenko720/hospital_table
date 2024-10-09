package lemon.hospitaltable.table.objects;

import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDate;

@Table("loves")
public record Love(
        @Id Long id,
        @With LocalDate date,
        @With @Column("patient_id") Long patientId,
        @With @Column("doctor_id") Integer doctorId,
        @With byte rating,
        @With String comment
) {
}
