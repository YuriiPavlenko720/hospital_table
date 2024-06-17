package lemon.hospitaltable.table.objects;

import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("wards")
public record Ward(
        @Id Integer id,
        @With Integer level,
        @With String name,
        @With @Column("department_id") Integer departmentId,
        @With Integer capacity
) {
}
