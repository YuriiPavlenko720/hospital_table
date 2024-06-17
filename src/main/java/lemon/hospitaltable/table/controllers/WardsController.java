package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.objects.Ward;
import lemon.hospitaltable.table.services.WardsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @GetMapping("/{id}")
    public Optional<Ward> getWard(@PathVariable Integer id) {
        return wardsService.findById(id);
    }

    @GetMapping
    public List<Ward> findAll() {
        return wardsService.findAll();
    }

    @GetMapping("/find_by_level")
    public List<Ward> findByLevel(@RequestParam Integer level) {
        return wardsService.findByLevel(level);
    }

    @GetMapping("/find_by_name")
    public List<Ward> findByName(@RequestParam String name) {
        return wardsService.findByName(name);
    }

    @GetMapping("/find_by_department")
    public List<Ward> findByDepartmentId(@RequestParam Integer departmentId) {
        return wardsService.findByDepartmentId(departmentId);
    }

    @GetMapping("/occupancy")
    public Map<Integer, WardsService.DepartmentsOccupancyStats> getWardsOccupancyStats(
            @RequestParam("date") LocalDate date) {
        return wardsService.getWardsOccupancyStats(date);
    }
}
