package lemon.hospitaltable.table.repositories;

import lemon.hospitaltable.table.objects.security.Role;
import org.springframework.data.repository.CrudRepository;

public interface RolesRepositoryInterface extends CrudRepository<Role, Short> {

}
