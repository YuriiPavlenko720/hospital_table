package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.objects.Treatment;
import lemon.hospitaltable.table.objects.TreatmentStats;
import lemon.hospitaltable.table.services.TreatmentsService;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/treatments")
public class TreatmentsController {

    private final TreatmentsService treatmentsService;

    public record TreatmentRequest(
            Long patientId,
            Integer doctorId,
            Integer wardId,
            LocalDate dateIn,
            LocalDate dateOut,
            String notation
    ) {
    }

    @PostMapping
    public void addTreatment(@RequestBody TreatmentRequest treatmentRequest) {
        treatmentsService.save(treatmentRequest);
    }

    @PostMapping("/{id}/delete")
    public void deleteById(@PathVariable Long id) {
        treatmentsService.deleteById(id);
    }

    @PostMapping("/{id}/change_patient_id")
    public void changePatientIdById(@PathVariable Long id, Long newPatientId) {
        treatmentsService.changePatientIdById(id, newPatientId);
    }

    @PostMapping("/{id}/change_doctor_id")
    public void changeDoctorIdById(@PathVariable Long id, Integer newDoctorId) {
        treatmentsService.changeDoctorIdById(id, newDoctorId);
    }

    @PostMapping("/{id}/change_ward_id")
    public void changeWardIdById(@PathVariable Long id, Integer newWardId) {
        treatmentsService.changeWardIdById(id, newWardId);
    }

    @PostMapping("/{id}/change_date_in")
    public void changeDateInById(@PathVariable Long id, LocalDate newDateIn) {
        treatmentsService.changeDateInById(id, newDateIn);
    }

    @PostMapping("/{id}/change_date_out")
    public void changeDateOutById(@PathVariable Long id, LocalDate newDateOut) {
        treatmentsService.changeDateOutById(id, newDateOut);
    }

    @PostMapping("/{id}/change_notation")
    public void changeNotationById(@PathVariable Long id, String newNotation) {
        treatmentsService.changeNotationById(id, newNotation);
    }

    @GetMapping("/stats")
    public ResponseEntity<ByteArrayResource> getTreatmentsStats(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate
    ) {
        List<TreatmentStats> stats = treatmentsService.getTreatmentsStatsByDepartments(startDate, endDate);
        Integer totalTreatments = treatmentsService.countTreatmentsStats(startDate, endDate);

        StringWriter writer = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader("DepartmentId", "Count")
                .build();

        try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
            for (TreatmentStats stat : stats) {
                csvPrinter.printRecord(stat.departmentId(), stat.count());
            }
            csvPrinter.printRecord("Total", totalTreatments);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ByteArrayResource resource = new ByteArrayResource(writer.toString().getBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"treating_stats.csv\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }

    @GetMapping("/stats_by_doctor")
    public ResponseEntity<ByteArrayResource> countTreatmentsStatsByDoctorId(
            @RequestParam("doctorId") Integer doctorId,
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate
    ) {
        Integer count = treatmentsService.countTreatmentsStatsByDoctorId(
                doctorId,
                startDate,
                endDate
        );

        StringWriter writer = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader("Doctor", "Count")
                .build();

        try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
            csvPrinter.printRecord(doctorId, count);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ByteArrayResource resource = new ByteArrayResource(writer.toString().getBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"doctor_treating_stats.csv\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }

    @GetMapping("/stats_by_department")
    public ResponseEntity<ByteArrayResource> countTreatmentsStatsByDepartmentId(
            @RequestParam("departmentId") Integer departmentId,
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate
    ) {
        Integer count = treatmentsService.countTreatmentsStatsByDepartmentId(
                departmentId,
                startDate,
                endDate
        );

        StringWriter writer = new StringWriter();
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader("Department", "Count")
                .build();

        try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
            csvPrinter.printRecord(departmentId, count);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ByteArrayResource resource = new ByteArrayResource(writer.toString().getBytes());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"department_treating_stats.csv\"")
                .contentType(MediaType.TEXT_PLAIN)
                .body(resource);
    }

    @GetMapping("/{id}")
    public Optional<Treatment> getTreatment(@PathVariable Long id) {
        return treatmentsService.findById(id);
    }

    @GetMapping
    public List<Treatment> findAll() {
        return treatmentsService.findAll();
    }

    @GetMapping("/find_by_patient")
    public List<Treatment> findByPatientId(@RequestParam Long patientId) {
        return treatmentsService.findByPatientId(patientId);
    }

    @GetMapping("/find_by_doctor")
    public List<Treatment> findByDoctorId(@RequestParam Integer doctorId) {
        return treatmentsService.findByDoctorId(doctorId);
    }

    @GetMapping("/find_by_ward")
    public List<Treatment> findByWardId(@RequestParam Integer wardId) {
        return treatmentsService.findByWardId(wardId);
    }
}
