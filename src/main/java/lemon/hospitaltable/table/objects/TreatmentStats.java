package lemon.hospitaltable.table.objects;

import org.springframework.data.relational.core.mapping.Column;

public record TreatmentStats(
        @Column("departmentId")Integer departmentId,
        Integer count
) {
}
