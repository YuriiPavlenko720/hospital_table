package lemon.hospitaltable.table.objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.sql.Date;

@Table("doctors")
public record Doctor (@Id Integer id, String name, Date birth, String position, @Column("department_id") Integer departmentId){

}
