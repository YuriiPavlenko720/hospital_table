package lemon.hospitaltable.table.services;

import lemon.hospitaltable.table.objects.Department;
import lemon.hospitaltable.table.repositories.DepartmentsRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentsService {

    private final DepartmentsRepositoryInterface departmentsRepository;

    @Autowired
    public DepartmentsService(DepartmentsRepositoryInterface departmentsRepository) {
        this.departmentsRepository = departmentsRepository;
    }

    public void save(String name) {
        Department department = new Department(null, name);
        departmentsRepository.save(department);
    }

    public void deleteById(Integer id) {
        departmentsRepository.deleteById(id);
    }

    public void renameById(Integer id, String name) {
        departmentsRepository.renameById(id, name);
    }

    public Optional<Department> findById(Integer id) {
        return departmentsRepository.findById(id);
    }

    public List<Department> findAll() {
        return (List<Department>) departmentsRepository.findAll();
    }
}
