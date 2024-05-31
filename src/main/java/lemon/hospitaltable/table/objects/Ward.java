package lemon.hospitaltable.table.objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("wards")
public record Ward(@Id Integer id, Integer level, String name, @Column("department_id") Integer departmentId,
                   Integer capacity, Integer taken, Integer free) {

}
