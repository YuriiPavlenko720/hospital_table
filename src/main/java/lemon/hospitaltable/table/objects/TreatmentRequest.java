package lemon.hospitaltable.table.objects;

import lombok.Getter;
import java.sql.Date;

@Getter
public class TreatmentRequest {
    private Long patientId;
    private Integer doctorId;
    private Integer wardId;
    private Date dateIn;
    private Date dateOut;
    private String notation;
}
