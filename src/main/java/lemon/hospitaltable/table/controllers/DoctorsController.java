package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.objects.Doctor;
import lemon.hospitaltable.table.services.DoctorsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/doctors")
public class DoctorsController {

    private final DoctorsService doctorsService;

    public record DoctorRequest (
            String name,
            LocalDate birth,
            String position,
            Integer departmentId,
            String email
    ) {
    }

    @PostMapping
    public void addDoctor(@RequestBody DoctorRequest doctorRequest) {
        doctorsService.save(doctorRequest);
    }

    @PostMapping("/{id}/delete")
    public void deleteById(@PathVariable Integer id) {
        doctorsService.deleteById(id);
    }

    @PostMapping("/{id}/change_name")
    public void renameById(@PathVariable Integer id, String newName) {
        doctorsService.renameById(id, newName);
    }

    @PostMapping("/{id}/change_birth")
    public void changeBirthById(@PathVariable Integer id, LocalDate newBirth) {
        doctorsService.changeBirthById(id, newBirth);
    }

    @PostMapping("/{id}/change_position")
    public void changePositionById(@PathVariable Integer id, String newPosition) {
        doctorsService.changePositionById(id, newPosition);
    }

    @PostMapping("/{id}/change_department")
    public void changeDepartmentById(@PathVariable Integer id, Integer newDepartmentId) {
        doctorsService.changeDepartmentById(id, newDepartmentId);
    }

    @PostMapping("/{id}/change_email")
    public void changeEmailById(@PathVariable Integer id, String newEmail) {
        doctorsService.changeEmailById(id, newEmail);
    }

    @GetMapping("/find")
    public ResponseEntity<?> findDoctors(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) String position
    ) {

        List<Doctor> doctors = doctorsService.findDoctors(id, name, departmentId, position);
        if (doctors.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(doctors);
        }
    }
}

