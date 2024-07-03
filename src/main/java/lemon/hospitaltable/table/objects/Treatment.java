package lemon.hospitaltable.table.objects;

import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDate;

@Table("treatments")
public record Treatment(
        @Id Long id,
        @With @Column("patient_id") Long patientId,
        @With @Column("doctor_id") Integer doctorId,
        @With @Column("ward_id") Integer wardId,
        @With @Column("date_in") LocalDate dateIn,
        @With @Column("date_out") LocalDate dateOut,
        @With String diagnosis,
        @With String notation
) {
}
