package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.objects.Ward;
import lemon.hospitaltable.table.objects.WardRequest;
import lemon.hospitaltable.table.services.WardsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Ward> addWard(@RequestBody WardRequest wardRequest) {
        Ward newWard = wardsService.save(wardRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newWard.id())
                .toUri();
        return ResponseEntity.created(location).body(newWard);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        wardsService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/change_level")
    public void changeLevelById(@PathVariable Integer id, Integer newLevel) {
        wardsService.changeLevelById(id, newLevel);
    }

    @PostMapping("/{id}/change_name")
    public void renameById(@PathVariable Integer id, String newName) {
        wardsService.renameById(id, newName);
    }

    @PostMapping("/{id}/change_department")
    public void changeDepartmentById(@PathVariable Integer id, Integer newDepartmentId) {
        wardsService.changeDepartmentById(id, newDepartmentId);
    }

    @PostMapping("/{id}/change_capacity")
    public void changeCapacityById(@PathVariable Integer id, Integer newCapacity) {
        wardsService.changeCapacityById(id, newCapacity);
    }

    @GetMapping("/{id}")
    public Optional<Ward> getWard(@PathVariable Integer id) {
        return wardsService.findById(id);
    }

    @GetMapping("/find")
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

    @GetMapping("/occupancy")
    public Map<String, WardsService.DepartmentsOccupancyStats> getWardsOccupancyStats(
            @RequestParam("date") LocalDate date) {
        return wardsService.getWardsOccupancyStats(date);
    }
}
