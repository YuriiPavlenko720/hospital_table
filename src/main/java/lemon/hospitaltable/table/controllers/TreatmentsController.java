package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.objects.Treatment;
import lemon.hospitaltable.table.objects.TreatmentRequest;
import lemon.hospitaltable.table.objects.TreatmentStats;
import lemon.hospitaltable.table.services.TreatmentsService;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/treatments")
public class TreatmentsController {

    private final TreatmentsService treatmentsService;

    @PostMapping
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<Treatment> addTreatment(@RequestBody TreatmentRequest treatmentRequest) {
        Treatment newTreatment = treatmentsService.save(treatmentRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newTreatment.id())
                .toUri();
        return ResponseEntity.created(location).body(newTreatment);
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        treatmentsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change_patient_id")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public void changePatientIdById(@PathVariable Long id, Long newPatientId) {
        treatmentsService.changePatientIdById(id, newPatientId);
    }

    @PostMapping("/{id}/change_doctor_id")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public void changeDoctorIdById(@PathVariable Long id, Integer newDoctorId) {
        treatmentsService.changeDoctorIdById(id, newDoctorId);
    }

    @PostMapping("/{id}/change_ward_id")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public void changeWardIdById(@PathVariable Long id, Integer newWardId) {
        treatmentsService.changeWardIdById(id, newWardId);
    }

    @PostMapping("/{id}/change_date_in")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public void changeDateInById(@PathVariable Long id, LocalDate newDateIn) {
        treatmentsService.changeDateInById(id, newDateIn);
    }

    @PostMapping("/{id}/change_date_out")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public void changeDateOutById(@PathVariable Long id, LocalDate newDateOut) {
        treatmentsService.changeDateOutById(id, newDateOut);
    }

    @PostMapping("/{id}/change_diagnosis")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public void changeDiagnosisById(@PathVariable Long id, String newDiagnosis) {
        treatmentsService.changeDiagnosisById(id, newDiagnosis);
    }

    @PostMapping("/{id}/change_notation")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public void changeNotationById(@PathVariable Long id, String newNotation) {
        treatmentsService.changeNotationById(id, newNotation);
    }

    @GetMapping("/stats")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
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
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
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
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
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
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER"})
    public Optional<Treatment> getTreatment(@PathVariable Long id) {
        return treatmentsService.findById(id);
    }

    @GetMapping("/find")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER"})
    public ResponseEntity<?> findTreatments(
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Integer doctorId,
            @RequestParam(required = false) Integer wardId
    ) {
        List<Treatment> treatments = treatmentsService.findTreatments(patientId, doctorId, wardId);
        if (treatments.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(treatments);
        }
    }
}
