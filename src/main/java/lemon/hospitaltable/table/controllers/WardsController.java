package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.objects.DepartmentsOccupancyStats;
import lemon.hospitaltable.table.objects.Ward;
import lemon.hospitaltable.table.objects.WardRequest;
import lemon.hospitaltable.table.services.WardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class WardsController {
    private final WardsService wardsService;

    @Autowired
    public WardsController(WardsService wardsService) {
        this.wardsService = wardsService;
    }

    @PostMapping("/api/wards")
    public void addWard(@RequestBody WardRequest wardRequest) {
        wardsService.save(wardRequest.getLevel(), wardRequest.getName(), wardRequest.getDepartmentId(),
                wardRequest.getCapacity());
    }

    @PostMapping("/api/wards/delete/{id}")
    public void deleteById(@PathVariable Integer id) {
        wardsService.deleteById(id);
    }

    @PostMapping("/api/wards/change_level/{id}")
    public void changeLevelById(@PathVariable Integer id, Integer level) {
        wardsService.changeLevelById(id, level);
    }

    @PostMapping("/api/wards/change_name/{id}")
    public void renameById(@PathVariable Integer id, String name) {
        wardsService.renameById(id, name);
    }

    @PostMapping("/api/wards/change_department/{id}")
    public void changeDepartmentById(@PathVariable Integer id, Integer departmentId) {
        wardsService.changeDepartmentById(id, departmentId);
    }

    @PostMapping("/api/wards/change_capacity/{id}")
    public void changeCapacityById(@PathVariable Integer id, Integer capacity) {
        wardsService.changeCapacityById(id, capacity);
    }

    @PostMapping("/api/wards/change_taken/{id}")
    public void changeTakenById(@PathVariable Integer id, Integer taken) {
        wardsService.changeTakenById(id, taken);
    }

    @PostMapping("/api/wards/change_free/{id}")
    public void changeFreeById(@PathVariable Integer id, Integer free) {
        wardsService.changeFreeById(id, free);
    }

    @GetMapping("/api/wards/{id}")
    public Optional<Ward> getward(@PathVariable Integer id) {
        return wardsService.findById(id);
    }

    @GetMapping("/api/wards/")
    public List<Ward> findAll() {
        return wardsService.findAll();
    }

    @GetMapping("/api/wards/find_by_level/{level}")
    public List<Ward> findByLevel(@PathVariable Integer level) {
        return wardsService.findByLevel(level);
    }

    @GetMapping("/api/wards/find_by_name/{name}")
    public List<Ward> findByName(@PathVariable String name) {
        return wardsService.findByName(name);
    }

    @GetMapping("/api/wards/show_by_department/{departmentId}")
    public List<Ward> findByDepartmentId(@PathVariable Integer departmentId) {
        return wardsService.findByDepartmentId(departmentId);
    }

    @GetMapping("/api/wards-occupancy")
    public Map<Integer, DepartmentsOccupancyStats> getWardsOccupancyStats(
            @RequestParam("date") Date date) {
        return wardsService.getWardsOccupancyStats(date);
    }
}
