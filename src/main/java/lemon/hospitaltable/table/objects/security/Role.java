package lemon.hospitaltable.table.objects.security;

import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("roles")
public record Role(
        @Id Short id,
        @With String name
) {
}
