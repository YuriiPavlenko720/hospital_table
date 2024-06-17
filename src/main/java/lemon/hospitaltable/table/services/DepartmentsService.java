package lemon.hospitaltable.table.services;

import lemon.hospitaltable.table.controllers.DepartmentsController;
import lemon.hospitaltable.table.objects.*;
import lemon.hospitaltable.table.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class DepartmentsService {

    private final DepartmentsRepositoryInterface departmentsRepository;
    private final DoctorsRepositoryInterface doctorsRepository;
    private final WardsRepositoryInterface wardsRepository;

    public void save(DepartmentsController.DepartmentRequest departmentRequest) {
        departmentsRepository.save(new Department(null, departmentRequest.name()));
    }

    public void deleteById(Integer id) {
        //checking existence of the aim department
        departmentsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department ID " + id + " not found."));

        //checking existence of wards in the aim department
        List<Ward> wardsOfDepartment = wardsRepository.findByDepartmentId(id);
        if (!wardsOfDepartment.isEmpty()) {
            throw new IllegalArgumentException("Cannot delete department with wards: " + wardsOfDepartment);
        }

        //checking existence of doctors in the aim department
        List<Doctor> doctorsOfDepartment = doctorsRepository.findByDepartmentId(id);
        if (!doctorsOfDepartment.isEmpty()) {
            throw new IllegalArgumentException("Cannot delete department with doctors: " + doctorsOfDepartment);
        }

        //deleting of the aim department
        departmentsRepository.deleteById(id);
    }


    public void renameById(Integer id, String newName) {
        Department department = departmentsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department ID " + id + " not found."));
        departmentsRepository.save(department.withName(newName));
    }

    public Optional<Department> findById(Integer id) {
        return departmentsRepository.findById(id);
    }

    public List<Department> findAll() {
        return (List<Department>) departmentsRepository.findAll();
    }
}
