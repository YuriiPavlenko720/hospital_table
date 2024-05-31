package lemon.hospitaltable.table.objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.sql.Date;

@Table("treatments")
public record Treatment(@Id Long id, @Column("patient_id") Long patientId, @Column("doctor_id") Integer doctorId,
                        @Column("ward_id") Integer wardId, Date dateIn, Date dateOut, String notation) {

}
