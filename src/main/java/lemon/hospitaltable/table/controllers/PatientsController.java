package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.objects.Patient;
import lemon.hospitaltable.table.objects.PatientRequest;
import lemon.hospitaltable.table.services.PatientsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Patient> addPatient(@RequestBody PatientRequest patientRequest) {
        Patient newPatient = patientsService.save(patientRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newPatient.id())
                .toUri();
        return ResponseEntity.created(location).body(newPatient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        patientsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change_name")
    public void renameById(@PathVariable Long id, String newName) {
        patientsService.renameById(id, newName);
    }

    @PostMapping("/{id}/change_birth")
    public void changeBirthById(@PathVariable Long id, LocalDate newBirth) {
        patientsService.changeBirthById(id, newBirth);
    }

    @PostMapping("/{id}/change_address")
    public void changeAddressById(@PathVariable Long id, String newAddress) {
        patientsService.changeAddressById(id, newAddress);
    }

    @PostMapping("/{id}/change_email")
    public void changeEmailById(@PathVariable Long id, String newEmail) {
        patientsService.changeEmailById(id, newEmail);
    }

    @PostMapping("/{id}/change_status")
    public void changeStatusById(@PathVariable Long id, String newStatus) {
        patientsService.changeStatusById(id, newStatus);
    }

    @PostMapping("/{id}/change_notation")
    public void changeNotationById(@PathVariable Long id, String newNotation) {
        patientsService.changeNotationById(id, newNotation);
    }

    @GetMapping("/{id}")
    public Optional<Patient> getPatient(@PathVariable Long id) {
        return patientsService.findById(id);
    }

    @GetMapping("/find")
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
