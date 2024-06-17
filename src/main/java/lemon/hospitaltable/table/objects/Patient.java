package lemon.hospitaltable.table.objects;

import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDate;

@Table("patients")
public record Patient(
        @Id Long id,
        @With String name,
        @With LocalDate birth,
        @With String address,
        @With String email,
        @With String status,
        @With String notation
) {
}
