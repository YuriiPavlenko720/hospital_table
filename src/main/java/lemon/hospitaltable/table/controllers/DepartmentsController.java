package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.objects.Department;
import lemon.hospitaltable.table.services.DepartmentsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/departments")
public class DepartmentsController {

    private final DepartmentsService departmentsService;

    public record DepartmentRequest(String name) {
    }

    @PostMapping
    public void createDepartment (@RequestBody DepartmentRequest departmentRequest) {
        departmentsService.save(departmentRequest);
    }

    @PostMapping("/{id}/delete")
    public void deleteById (@PathVariable Integer id) {
        departmentsService.deleteById(id);
    }

    @PostMapping("/{id}/rename")
    public void renameById (@PathVariable Integer id, String newName) {
        departmentsService.renameById(id, newName);
    }

    @GetMapping("/{id}")
    public Optional<Department> getDepartment (@PathVariable Integer id) {
        return departmentsService.findById(id);
    }

    @GetMapping("/find")
    public ResponseEntity<?> findDoctors(
            @RequestParam(required = false) String name
            ) {
        List<Department> departments = departmentsService.findDepartments(name);
        if (departments.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(departments);
        }
    }
}
