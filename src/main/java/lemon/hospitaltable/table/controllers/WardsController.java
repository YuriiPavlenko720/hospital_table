package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.objects.Ward;
import lemon.hospitaltable.table.objects.WardRequest;
import lemon.hospitaltable.table.services.WardsService;
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
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/wards")
public class WardsController {

    private final WardsService wardsService;

    @PostMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Ward> addWard(@RequestBody WardRequest wardRequest) {
        Ward newWard = wardsService.save(wardRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newWard.id())
                .toUri();
        return ResponseEntity.created(location).body(newWard);
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        wardsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change_level")
    @Secured({"ROLE_ADMIN"})
    public void changeLevelById(@PathVariable Integer id, Integer newLevel) {
        wardsService.changeLevelById(id, newLevel);
    }

    @PostMapping("/{id}/change_name")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public void renameById(@PathVariable Integer id, String newName) {
        wardsService.renameById(id, newName);
    }

    @PostMapping("/{id}/change_department")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public void changeDepartmentById(@PathVariable Integer id, Integer newDepartmentId) {
        wardsService.changeDepartmentById(id, newDepartmentId);
    }

    @PostMapping("/{id}/change_capacity")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public void changeCapacityById(@PathVariable Integer id, Integer newCapacity) {
        wardsService.changeCapacityById(id, newCapacity);
    }

    @GetMapping("/{id}")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public Optional<Ward> getWard(@PathVariable Integer id) {
        return wardsService.findById(id);
    }

    @GetMapping("/find")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<?> findWards(
            @RequestParam(required = false) Integer level,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer departmentId
    ) {

        List<Ward> wards = wardsService.findWards(level, name, departmentId);
        if (wards.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(wards);
        }
    }

    @GetMapping("/wards_occupancy")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public Map<String, WardsService.DepartmentsOccupancyStats> getWardsOccupancyStats(
            @RequestParam("date") LocalDate date
    ) {
        return wardsService.getWardsOccupancyStats(date);
    }

    @GetMapping("/wards_availability")
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    public ResponseEntity<String> getWardsAvailabilityStats(
            @RequestParam("startDate") LocalDate startDate,
            @RequestParam("endDate") LocalDate endDate) {
        String csvContent = wardsService.getWardsAvailabilityStats(startDate, endDate);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=wards_availability.csv");
        headers.setContentType(MediaType.TEXT_PLAIN);

        return ResponseEntity.ok()
                .headers(headers)
                .body(csvContent);
    }
}
