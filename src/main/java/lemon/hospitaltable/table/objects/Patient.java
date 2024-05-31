package lemon.hospitaltable.table.objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.sql.Date;

@Table("patients")
public record Patient(@Id Long id, String name, Date birth, String address, String status, String notation) {

}
