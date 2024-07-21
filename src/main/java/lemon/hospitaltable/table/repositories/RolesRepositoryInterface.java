package lemon.hospitaltable.table.repositories;

import lemon.hospitaltable.table.objects.Role;
import org.springframework.data.repository.CrudRepository;

public interface RolesRepositoryInterface extends CrudRepository<Role, Short> {
}
