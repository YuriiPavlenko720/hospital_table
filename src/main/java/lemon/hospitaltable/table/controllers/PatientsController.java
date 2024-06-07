package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.objects.Patient;
import lemon.hospitaltable.table.objects.PatientRequest;
import lemon.hospitaltable.table.services.PatientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class PatientsController {
    private final PatientsService patientsService;

    @Autowired
    public PatientsController(PatientsService patientsService) {
        this.patientsService = patientsService;
    }

    @PostMapping("/api/patients")
    public void addPatient(@RequestBody PatientRequest patientRequest) {
        patientsService.save(patientRequest);
    }

    @PostMapping("/api/patients/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        patientsService.deleteById(id);
    }

    @PostMapping("/api/patients/change_name/{id}")
    public void renameById(@PathVariable Long id, String name) {
        patientsService.renameById(id, name);
    }

    @PostMapping("/api/patients/change_birth/{id}")
    public void changeBirthById(@PathVariable Long id, Date birth) {
        patientsService.changeBirthById(id, birth);
    }

    @PostMapping("/api/patients/change_address/{id}")
    public void changeAddressById(@PathVariable Long id, String address) {
        patientsService.changeAddressById(id, address);
    }

    @PostMapping("/api/patients/change_status/{id}")
    public void changeStatusById(@PathVariable Long id, String status) {
        patientsService.changeStatusById(id, status);
    }

    @PostMapping("/api/patients/change_notation/{id}")
    public void changeNotationById(@PathVariable Long id, String notation) {
        patientsService.changeNotationById(id, notation);
    }

    @GetMapping("/api/patients/{id}")
    public Optional<Patient> getPatient(@PathVariable Long id) {
        return patientsService.findById(id);
    }

    @GetMapping("/api/patients/")
    public List<Patient> findAll() {
        return patientsService.findAll();
    }

    @GetMapping("/api/patients/find_by_name/{name}")
    public List<Patient> findByName(@PathVariable String name) {
        return patientsService.findByName(name);
    }

    @GetMapping("/api/patients/show_by_status/{status}")
    public List<Patient> findByStatus(@PathVariable String status) {
        return patientsService.findByStatus(status);
    }
}
