package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.objects.Ward;
import lemon.hospitaltable.table.services.WardsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api/wards")
public class WardsController {

    private final WardsService wardsService;

    public record WardRequest (
        Integer level,
        String name,
        Integer departmentId,
        Integer capacity
    ) {
    }

    @PostMapping
    public void addWard(@RequestBody WardRequest wardRequest) {
        wardsService.save(wardRequest);
    }

    @PostMapping("/{id}/delete")
    public void deleteById(@PathVariable Integer id) {
        wardsService.deleteById(id);
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

    @GetMapping("/find")
    public ResponseEntity<?> findWards(
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer level,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer departmentId
    ) {

        List<Ward> wards = wardsService.findWards(id, level, name, departmentId);
        if (wards.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(wards);
        }
    }

    @GetMapping("/occupancy")
    public Map<Integer, WardsService.DepartmentsOccupancyStats> getWardsOccupancyStats(
            @RequestParam("date") LocalDate date) {
        return wardsService.getWardsOccupancyStats(date);
    }
}
