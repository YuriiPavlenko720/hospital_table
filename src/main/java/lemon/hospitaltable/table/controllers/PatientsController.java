package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.objects.Patient;
import lemon.hospitaltable.table.services.PatientsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/patients")
public class PatientsController {

    private final PatientsService patientsService;

    public record PatientRequest (
            String name,
            LocalDate birth,
            String address,
            String email,
            String status,
            String notation
    ) {
    }

    @PostMapping
    public void addPatient(@RequestBody PatientRequest patientRequest) {
        patientsService.save(patientRequest);
    }

    @PostMapping("/{id}/delete")
    public void deleteById(@PathVariable Long id) {
        patientsService.deleteById(id);
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

    @GetMapping
    public List<Patient> findAll() {
        return patientsService.findAll();
    }

    @GetMapping("/find_by_name")
    public List<Patient> findByName(@RequestParam String name) {
        return patientsService.findByName(name);
    }

    @GetMapping("/find_by_status")
    public List<Patient> findByStatus(@RequestParam String status) {
        return patientsService.findByStatus(status);
    }
}
