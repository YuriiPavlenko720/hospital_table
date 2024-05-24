package lemon.hospitaltable.table.repositories;

import lemon.hospitaltable.table.objects.Department;
import java.util.List;

public interface DepartmentsRepositoryInterface {

    void save(Department department);

    void deleteById(int id);

    Department findById(int id);

    List<Department> findAll();

    void setDepartmentName(int id, String name);

}
