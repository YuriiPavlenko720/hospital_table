package lemon.hospitaltable.table.services;

import lemon.hospitaltable.table.objects.Doctor;
import lemon.hospitaltable.table.repositories.DoctorsRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorsService {
    private final DoctorsRepositoryInterface doctorsRepository;

    @Autowired
    public DoctorsService(DoctorsRepositoryInterface doctorsRepository) {
        this.doctorsRepository = doctorsRepository;
    }

    public void save(String name, Date birth, String position, Integer departmentId) {
        Doctor doctor = new Doctor(null, name, birth, position, departmentId);
        doctorsRepository.save(doctor);
    }

    public void deleteById(Integer id) {
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
