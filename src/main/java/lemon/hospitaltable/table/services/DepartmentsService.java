package lemon.hospitaltable.table.services;

import lemon.hospitaltable.table.objects.*;
import lemon.hospitaltable.table.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentsService {

    private final DepartmentsRepositoryInterface departmentsRepository;
    private final TreatmentsRepositoryInterface treatmentsRepository;
    private final DoctorsRepositoryInterface doctorsRepository;
    private final WardsRepositoryInterface wardsRepository;

    @Autowired
    public DepartmentsService(DepartmentsRepositoryInterface departmentsRepository,
                              TreatmentsRepositoryInterface treatmentsRepository,
                              DoctorsRepositoryInterface doctorsRepository,
                              WardsRepositoryInterface wardsRepository) {
        this.departmentsRepository = departmentsRepository;
        this.treatmentsRepository = treatmentsRepository;
        this.doctorsRepository = doctorsRepository;
        this.wardsRepository = wardsRepository;
    }

    public void save(String name) {
        Department department = new Department(null, name);
        departmentsRepository.save(department);
    }

    public void deleteById(Integer id) {
        //checking existing of the aim department
        Department department = departmentsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found."));

        //checking wards existing in the aim department
        List<Ward> wardsOfDepartment = wardsRepository.findByDepartmentId(id);
        if (!wardsOfDepartment.isEmpty()) {
            throw new IllegalArgumentException("Cannot delete department with wards.");
        }

        //checking doctors existing in the aim department
        List<Doctor> doctorsOfDepartment = doctorsRepository.findByDepartmentId(id);
        if (!doctorsOfDepartment.isEmpty()) {
            throw new IllegalArgumentException("Cannot delete department with doctors.");
        }

        //checking treatments existing in the aim department (probably don`t needed because of bounding doctor-treatment)
        List<Doctor> doctors = doctorsRepository.findByDepartmentId(id);
        for (Doctor doctor : doctors) {
            List<Treatment> treatments = treatmentsRepository.findByDoctorId(doctor.id());
            for (Treatment treatment : treatments) {
                if (treatment.dateOut().after(new Date(System.currentTimeMillis()))) {
                    throw new IllegalArgumentException(
                            "Cannot delete department with active or planned treatments in this department.");
                }
            }
        }

        //deleting of the aim department
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
