package lemon.hospitaltable.table.repositories;

import lemon.hospitaltable.table.objects.Ward;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface WardsRepositoryInterface extends CrudRepository<Ward, Integer> {

    @Modifying
    @Query("UPDATE wards SET level = :level WHERE id = :id")
    void changeLevelById(Integer id, Integer level);

    @Modifying
    @Query("UPDATE wards SET name = :name WHERE id = :id")
    void renameById(Integer id, String name);

    @Modifying
    @Query("UPDATE wards SET department_id = :departmentId WHERE id = :id")
    void changeDepartmentById(Integer id, Integer departmentId);

    @Modifying
    @Query("UPDATE wards SET capacity = :capacity WHERE id = :id")
    void changeCapacityById(Integer id, Integer capacity);

    @Modifying
    @Query("UPDATE wards SET taken = :taken WHERE id = :id")
    void changeTakenById(Integer id, Integer taken);

    @Modifying
    @Query("UPDATE wards SET free = :free WHERE id = :id")
    void changeFreeById(Integer id, Integer free);

    List<Ward> findByLevel(Integer level);

    List<Ward> findByName(String name);

    List<Ward> findByDepartmentId(Integer departmentId);
}
