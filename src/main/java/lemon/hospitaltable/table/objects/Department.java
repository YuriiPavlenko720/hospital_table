package lemon.hospitaltable.table.objects;

import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("departments")
public record Department(
        @Id Integer id,
        @With String name
) {

}
