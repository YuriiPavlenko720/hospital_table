package lemon.hospitaltable.table.objects;

import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDate;

@Table("doctors")
public record Doctor (
        @Id Integer id,
        @With String name,
        @With LocalDate birth,
        @With String position,
        @With @Column("department_id") Integer departmentId,
        @With String email
) {
}
