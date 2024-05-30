package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.objects.Doctor;
import lemon.hospitaltable.table.objects.DoctorRequest;
import lemon.hospitaltable.table.services.DoctorsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class DoctorsController {
    private final DoctorsService doctorsService;

    @Autowired
    public DoctorsController(DoctorsService doctorsService) {
        this.doctorsService = doctorsService;
    }

    @PostMapping("/api/doctors")
    public void addDoctor(@RequestBody DoctorRequest doctorRequest) {
        doctorsService.save(doctorRequest.getName(), doctorRequest.getBirth(),
                doctorRequest.getPosition(), doctorRequest.getDepartmentId());
    }

    @PostMapping("/api/doctors/delete/{id}")
    public void deleteById(@PathVariable Integer id) {
        doctorsService.deleteById(id);
    }

    @PostMapping("/api/doctors/change_name/{id}")
    public void renameById(@PathVariable Integer id, String name) {
        doctorsService.renameById(id, name);
    }

    @PostMapping("/api/doctors/change_birth/{id}")
    public void changeBirthById(@PathVariable Integer id, Date birth) {
        doctorsService.changeBirthById(id, birth);
    }

    @PostMapping("/api/doctors/change_position/{id}")
    public void changePositionById(@PathVariable Integer id, String position) {
        doctorsService.changePositionById(id, position);
    }

    @PostMapping("/api/doctors/change_department/{id}")
    public void changeDepartmentById(@PathVariable Integer id, Integer departmentId) {
        doctorsService.changeDepartmentById(id, departmentId);
    }

    @GetMapping("/api/doctors/{id}")
    public Optional<Doctor> getDoctor(@PathVariable Integer id) {
        return doctorsService.findById(id);
    }

    @GetMapping("/api/doctors/")
    public List<Doctor> findAll() {
        return doctorsService.findAll();
    }

    @GetMapping("/api/doctors/find_by_name/{name}")
    public List<Doctor> findByName(@PathVariable String name) {
        return doctorsService.findByName(name);
    }

    @GetMapping("/api/doctors/show_by_department/{departmentId}")
    public List<Doctor> findByDepartmentId(@PathVariable Integer departmentId) {
        return doctorsService.findByDepartmentId(departmentId);
    }

    @GetMapping("/api/doctors/show_by_position/{position}")
    public List<Doctor> findByPosition(@PathVariable String position) {
        return doctorsService.findByPosition(position);
    }
}

