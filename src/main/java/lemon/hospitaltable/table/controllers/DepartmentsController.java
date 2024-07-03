package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.objects.Department;
import lemon.hospitaltable.table.objects.DepartmentRequest;
import lemon.hospitaltable.table.services.DepartmentsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/departments")
public class DepartmentsController {

    private final DepartmentsService departmentsService;

    @PostMapping
    public ResponseEntity<Department> createDepartment (@RequestBody DepartmentRequest departmentRequest) {
        Department newDepartment = departmentsService.save(departmentRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newDepartment.id())
                .toUri();
        return ResponseEntity.created(location).body(newDepartment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById (@PathVariable Integer id) {
        departmentsService.deleteById(id);
        return ResponseEntity.noContent().build();
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
