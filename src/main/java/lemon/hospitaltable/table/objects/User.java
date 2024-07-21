package lemon.hospitaltable.table.objects;

import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
public record User(
        @Id Long id,
        @With String username,
        @With String email,
        @With @Column("role_id") Short roleId
) {
}
