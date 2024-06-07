package lemon.hospitaltable.table.objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Date;

@Table("treatments")
public record Treatment(
        @Id Long id,
        @Column("patient_id") Long patientId,
        @Column("doctor_id") Integer doctorId,
        @Column("ward_id") Integer wardId,
        @Column("dateIn") Date dateIn,
        @Column("dateOut") Date dateOut,
        String notation
) {

    public static Treatment fromRequest(TreatmentRequest tr) {
        return new Treatment(
                null,
                tr.getPatientId(),
                tr.getDoctorId(),
                tr.getWardId(),
                tr.getDateIn(),
                tr.getDateOut(),
                tr.getNotation()
        );
    }


}
