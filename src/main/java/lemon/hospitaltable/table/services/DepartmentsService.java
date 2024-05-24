package lemon.hospitaltable.table.services;

import lemon.hospitaltable.table.objects.Department;
import lemon.hospitaltable.table.repositories.DepartmentsRepositoryInterface;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DepartmentsService {

    private final DepartmentsRepositoryInterface departmentsRepository;

    public DepartmentsService(DepartmentsRepositoryInterface departmentsRepository) {
        this.departmentsRepository = departmentsRepository;
    }

    public void save(Department department) {
        departmentsRepository.save(department);
    }

    public void deleteById(int id) {
        departmentsRepository.deleteById(id);
    }

    public Department findById(int id){
        return departmentsRepository.findById(id);
    }

    public List<Department> findAll(){
        return departmentsRepository.findAll();
    }

    public void setDepartmentName(int id, String name) {
        departmentsRepository.setDepartmentName(id, name);
    }
}
