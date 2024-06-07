package lemon.hospitaltable.table.services;

import lemon.hospitaltable.table.objects.*;
import lemon.hospitaltable.table.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class DepartmentsService {

    private final DepartmentsRepositoryInterface departmentsRepository;
    private final TreatmentsRepositoryInterface treatmentsRepository;
    private final DoctorsRepositoryInterface doctorsRepository;
    private final WardsRepositoryInterface wardsRepository;

    public void save(String name) {
        Department department = new Department(null, name);
        departmentsRepository.save(department);
    }

    public void deleteById(Integer id) {
        //checking existing of the aim department
        departmentsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found."));

        //checking wards existing in the aim department
        List<Ward> wardsOfDepartment = wardsRepository.findByDepartmentId(id);
        if (!wardsOfDepartment.isEmpty()) {
            throw new IllegalArgumentException("Cannot delete department with wards: " + wardsOfDepartment);
        }

        //checking doctors existing in the aim department
        List<Doctor> doctorsOfDepartment = doctorsRepository.findByDepartmentId(id);
        if (!doctorsOfDepartment.isEmpty()) {
            throw new IllegalArgumentException("Cannot delete department with doctors: " + doctorsOfDepartment);
        }

        //deleting of the aim department
        departmentsRepository.deleteById(id);
    }

    public void renameById(Integer id, String name) {
        Department department = departmentsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found."));
        departmentsRepository.save(
                department.withName(name)
        );
    }

    public Optional<Department> findById(Integer id) {
        return departmentsRepository.findById(id);
    }

    public List<Department> findAll() {
        return (List<Department>) departmentsRepository.findAll();
    }
}
