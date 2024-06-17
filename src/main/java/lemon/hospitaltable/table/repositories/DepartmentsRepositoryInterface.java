package lemon.hospitaltable.table.repositories;

import lemon.hospitaltable.table.objects.Department;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentsRepositoryInterface extends CrudRepository<Department, Integer> {

}
