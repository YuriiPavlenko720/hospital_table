package lemon.hospitaltable.table.objects;

import lombok.Getter;
import java.sql.Date;

@Getter
public class PatientRequest {
    private String name;
    private Date birth;
    private String address;
    private String status;
    private String notation;
}
