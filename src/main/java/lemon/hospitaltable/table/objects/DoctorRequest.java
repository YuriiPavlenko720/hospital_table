package lemon.hospitaltable.table.objects;

import lombok.Getter;
import java.sql.Date;

@Getter
public class DoctorRequest {
    private String name;
    private Date birth;
    private String position;
    private Integer departmentId;
}
