package lemon.hospitaltable.table.repositories;
import lemon.hospitaltable.table.objects.Department;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

public interface DepartmentsRepositoryInterface extends CrudRepository<Department, Integer> {

    @Modifying
    @Query("UPDATE departments SET name = :name WHERE id = :id")
    void renameById(Integer id, String name);
}
