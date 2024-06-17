package lemon.hospitaltable.table.repositories;

import lemon.hospitaltable.table.objects.Doctor;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface DoctorsRepositoryInterface extends CrudRepository<Doctor, Integer> {

    List<Doctor> findByName(String name);


    List<Doctor> findByDepartmentId(Integer departmentId);


    List<Doctor> findByPosition(String position);
}
