package lemon.hospitaltable.table.repositories;

import lemon.hospitaltable.table.objects.Department;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface DepartmentsRepositoryInterface extends CrudRepository<Department, Integer> {

    List<Department> findByName(String name);
}
