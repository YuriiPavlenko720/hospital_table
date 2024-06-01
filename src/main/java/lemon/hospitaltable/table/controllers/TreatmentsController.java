package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.objects.Treatment;
import lemon.hospitaltable.table.objects.TreatmentRequest;
import lemon.hospitaltable.table.services.TreatmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class TreatmentsController {
    private final TreatmentsService treatmentsService;

    @Autowired
    public TreatmentsController(TreatmentsService treatmentsService) {
        this.treatmentsService = treatmentsService;
    }

    @PostMapping("/api/treatments")
    public void addTreatment(@RequestBody TreatmentRequest treatmentRequest) {
        treatmentsService.save(treatmentRequest.getPatientId(), treatmentRequest.getDoctorId(),
                treatmentRequest.getWardId(), treatmentRequest.getDateIn(),
                treatmentRequest.getDateOut(), treatmentRequest.getNotation());
    }

    @PostMapping("/api/treatments/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        treatmentsService.deleteById(id);
    }

    @PostMapping("/api/treatments/change_patientId/{id}")
    public void changePatientIdById(@PathVariable Long id, Long patientId) {
        treatmentsService.changePatientIdById(id, patientId);
    }

    @PostMapping("/api/treatments/change_doctorId/{id}")
    public void changeDoctorIdById(@PathVariable Long id, Integer doctorId) {
        treatmentsService.changeDoctorIdById(id, doctorId);
    }

    @PostMapping("/api/treatments/change_wardId/{id}")
    public void changeWardIdById(@PathVariable Long id, Integer wardId) {
        treatmentsService.changeWardIdById(id, wardId);
    }

    @PostMapping("/api/treatments/change_dateIn/{id}")
    public void changeDateInById(@PathVariable Long id, Date dateIn) {
        treatmentsService.changeDateInById(id, dateIn);
    }

    @PostMapping("/api/treatments/change_dateOut/{id}")
    public void changeDateOutById(@PathVariable Long id, Date dateOut) {
        treatmentsService.changeDateOutById(id, dateOut);
    }

    @PostMapping("/api/treatments/change_notation/{id}")
    public void changeNotationById(@PathVariable Long id, String notation) {
        treatmentsService.changeNotationById(id, notation);
    }

    @GetMapping("/api/treatments/{id}")
    public Optional<Treatment> getTreatment(@PathVariable Long id) {
        return treatmentsService.findById(id);
    }

    @GetMapping("/api/treatments/")
    public List<Treatment> findAll() {
        return treatmentsService.findAll();
    }

    @GetMapping("/api/treatments/find_by_patient_id/{patientId}")
    public List<Treatment> findByPatientId(@PathVariable Long patientId) {
        return treatmentsService.findByPatientId(patientId);
    }

    @GetMapping("/api/treatments/find_by_doctor_id/{doctorId}")
    public List<Treatment> findByDoctorId(@PathVariable Integer doctorId) {
        return treatmentsService.findByDoctorId(doctorId);
    }

    @GetMapping("/api/treatments/find_by_ward_id/{wardId}")
    public List<Treatment> findByWardId(@PathVariable Integer wardId) {
        return treatmentsService.findByWardId(wardId);
    }
}
