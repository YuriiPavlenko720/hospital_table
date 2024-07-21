package lemon.hospitaltable.table.repositories;

import lemon.hospitaltable.table.objects.User;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UsersRepositoryInterface extends CrudRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
