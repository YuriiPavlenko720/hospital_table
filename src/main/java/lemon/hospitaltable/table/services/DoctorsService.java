package lemon.hospitaltable.table.services;

import lemon.hospitaltable.table.objects.Doctor;
import lemon.hospitaltable.table.objects.Treatment;
import lemon.hospitaltable.table.repositories.DoctorsRepositoryInterface;
import lemon.hospitaltable.table.repositories.TreatmentsRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorsService {
    private final DoctorsRepositoryInterface doctorsRepository;
    private final TreatmentsRepositoryInterface treatmentsRepository;

    @Autowired
    public DoctorsService(DoctorsRepositoryInterface doctorsRepository, TreatmentsRepositoryInterface treatmentsRepository) {
        this.doctorsRepository = doctorsRepository;
        this.treatmentsRepository = treatmentsRepository;
    }

    public void save(String name, Date birth, String position, Integer departmentId) {
        Doctor doctor = new Doctor(null, name, birth, position, departmentId);
        doctorsRepository.save(doctor);
    }

    public void deleteById(Integer id) {
        //checking existing of the aim doctor
        Doctor doctor = doctorsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found."));

        //checking treatments existing at the aim doctor
        List<Treatment> treatments = treatmentsRepository.findByDoctorId(id);
        for (Treatment treatment : treatments) {
            if (treatment.dateOut().after(new Date(System.currentTimeMillis()))) {
                throw new IllegalArgumentException("Cannot delete doctor with active or planned treatments at the doctor.");
            }
        }

        //deleting of the aim doctor
        doctorsRepository.deleteById(id);
    }

    public void renameById(Integer id, String name) {
        doctorsRepository.renameById(id, name);
    }

    public void changeBirthById(Integer id, Date birth) {
        doctorsRepository.changeBirthById(id, birth);
    }

    public void changePositionById(Integer id, String position) {
        doctorsRepository.changePositionById(id, position);
    }

    public void changeDepartmentById(Integer id, Integer departmentId) {
        //checking existing of the aim doctor
        Doctor doctor = doctorsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found."));

        //checking treatments existing at the aim doctor
        List<Treatment> treatments = treatmentsRepository.findByDoctorId(id);
        for (Treatment treatment : treatments) {
            if (treatment.dateOut().after(new Date(System.currentTimeMillis()))) {
                throw new IllegalArgumentException("Cannot change department of doctor with active or planned treatments at the doctor.");
            }
        }

        //changing department of the aim doctor
        doctorsRepository.changeDepartmentById(id, departmentId);
    }

    public Optional<Doctor> findById(Integer id) {
        return doctorsRepository.findById(id);
    }

    public List<Doctor> findAll() {
        return (List<Doctor>) doctorsRepository.findAll();
    }

    public List<Doctor> findByName(String name) {
        return doctorsRepository.findByName(name);
    }

    public List<Doctor> findByDepartmentId(Integer departmentId) {
        return doctorsRepository.findByDepartmentId(departmentId);
    }

    public List<Doctor> findByPosition(String position) {
        return doctorsRepository.findByPosition(position);
    }
}
