package lemon.hospitaltable.table.objects;

import lombok.Getter;
import java.sql.Date;

@Getter
public class TreatmentRequest {
    private Integer patient_id;
    private Integer doctor_id;
    private Integer ward_id;
    private Date date_in;
    private Date date_out;
    private String notation;
}
