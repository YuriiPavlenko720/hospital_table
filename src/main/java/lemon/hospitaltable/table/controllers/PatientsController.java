package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.objects.Patient;
import lemon.hospitaltable.table.objects.PatientRequest;
import lemon.hospitaltable.table.services.PatientsService;
import lombok.AllArgsConstructor;
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
@RequestMapping("/api/patients")
public class PatientsController {

    private final PatientsService patientsService;

    @PostMapping
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<Patient> addPatient(@RequestBody PatientRequest patientRequest) {
        Patient newPatient = patientsService.save(patientRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newPatient.id())
                .toUri();
        return ResponseEntity.created(location).body(newPatient);
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        patientsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change_name")
    @Secured({"ROLE_ADMIN"})
    public void renameById(@PathVariable Long id, String newName) {
        patientsService.renameById(id, newName);
    }

    @PostMapping("/{id}/change_birth")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public void changeBirthById(@PathVariable Long id, LocalDate newBirth) {
        patientsService.changeBirthById(id, newBirth);
    }

    @PostMapping("/{id}/change_address")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public void changeAddressById(@PathVariable Long id, String newAddress) {
        patientsService.changeAddressById(id, newAddress);
    }

    @PostMapping("/{id}/change_email")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public void changeEmailById(@PathVariable Long id, String newEmail) {
        patientsService.changeEmailById(id, newEmail);
    }

    @PostMapping("/{id}/change_status")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public void changeStatusById(@PathVariable Long id, String newStatus) {
        patientsService.changeStatusById(id, newStatus);
    }

    @PostMapping("/{id}/change_notation")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public void changeNotationById(@PathVariable Long id, String newNotation) {
        patientsService.changeNotationById(id, newNotation);
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER"})
    public Optional<Patient> getPatient(@PathVariable Long id) {
        return patientsService.findById(id);
    }

    @GetMapping("/find")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER"})
    public ResponseEntity<?> findPatients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String status
    ) {

        List<Patient> patients = patientsService.findPatients(name, status);
        if (patients.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(patients);
        }
    }
}
