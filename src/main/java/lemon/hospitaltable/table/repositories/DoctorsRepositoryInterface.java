package lemon.hospitaltable.table.repositories;

import lemon.hospitaltable.table.objects.Doctor;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import java.sql.Date;
import java.util.List;

public interface DoctorsRepositoryInterface extends CrudRepository<Doctor, Integer> {
    @Modifying
    @Query("UPDATE doctors SET name = :name WHERE id = :id")
    void renameById(Integer id, String name);

    @Modifying
    @Query("UPDATE doctors SET birth = :birth WHERE id = :id")
    void changeBirthById(Integer id, Date birth);

    @Modifying
    @Query("UPDATE doctors SET position = :position WHERE id = :id")
    void changePositionById(Integer id, String position);

    @Modifying
    @Query("UPDATE doctors SET department_id = :departmentId WHERE id = :id")
    void changeDepartmentById(Integer id, Integer departmentId);

    List<Doctor> findByName(String name);

    List<Doctor> findByDepartmentId(Integer departmentId);

    List<Doctor> findByPosition(String position);
}
