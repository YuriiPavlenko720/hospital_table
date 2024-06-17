package lemon.hospitaltable.table.repositories;

import lemon.hospitaltable.table.objects.Ward;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface WardsRepositoryInterface extends CrudRepository<Ward, Integer> {

    @Query("""
            SELECT
                capacity
            FROM
                wards
            WHERE
                id = :wardId
            """)
    Integer getCapacityById(@Param("wardId") Integer wardId);


    List<Ward> findByLevel(Integer level);


    List<Ward> findByName(String name);


    List<Ward> findByDepartmentId(Integer departmentId);
}
