package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.objects.Doctor;
import lemon.hospitaltable.table.objects.DoctorRequest;
import lemon.hospitaltable.table.services.DoctorsService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/doctors")
public class DoctorsController {

    private final DoctorsService doctorsService;

    @PostMapping
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<Doctor> addDoctor(@RequestBody DoctorRequest doctorRequest) {
        Doctor newDoctor = doctorsService.save(doctorRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newDoctor.id())
                .toUri();
        return ResponseEntity.created(location).body(newDoctor);
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        doctorsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change_name")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public void renameById(@PathVariable Integer id, String newName) {
        doctorsService.renameById(id, newName);
    }

    @PostMapping("/{id}/change_birth")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public void changeBirthById(@PathVariable Integer id, LocalDate newBirth) {
        doctorsService.changeBirthById(id, newBirth);
    }

    @PostMapping("/{id}/change_position")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public void changePositionById(@PathVariable Integer id, String newPosition) {
        doctorsService.changePositionById(id, newPosition);
    }

    @PostMapping("/{id}/change_department")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public void changeDepartmentById(@PathVariable Integer id, Integer newDepartmentId) {
        doctorsService.changeDepartmentById(id, newDepartmentId);
    }

    @PostMapping("/{id}/change_email")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public void changeEmailById(@PathVariable Integer id, String newEmail) {
        doctorsService.changeEmailById(id, newEmail);
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public Optional<Doctor> getDoctor(@PathVariable Integer id) {
        return doctorsService.findById(id);
    }

    @GetMapping("/find")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<?> findDoctors(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer departmentId,
            @RequestParam(required = false) String position
    ) {

        List<Doctor> doctors = doctorsService.findDoctors(name, departmentId, position);
        if (doctors.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(doctors);
        }
    }

    @GetMapping("/doctor_occupancy")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<String> getDoctorsOccupancyStats(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {
        String csvContent = doctorsService.getDoctorsOccupancyStats(startDate, endDate);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=doctors_occupancy.csv");
        headers.setContentType(MediaType.TEXT_PLAIN);
        return ResponseEntity.ok()
                .headers(headers)
                .body(csvContent);
    }
}

