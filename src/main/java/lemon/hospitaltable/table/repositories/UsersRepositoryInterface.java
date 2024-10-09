package lemon.hospitaltable.table.repositories;

import lemon.hospitaltable.table.objects.security.User;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface UsersRepositoryInterface extends CrudRepository<User, Long> {

    List<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByRoleId(Short roleId);

    Optional<User> findByEmailAndRoleId(String email, Short roleId);

    long countByRoleId(Short roleId);
}
