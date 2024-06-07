package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.objects.Department;
import lemon.hospitaltable.table.services.DepartmentsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/departments")
public class DepartmentsController {

    private final DepartmentsService departmentsService;

    public record DepartmentRequest(String name) { }

    @PostMapping
    public void createDepartment (@RequestBody DepartmentRequest departmentRequest) {
        departmentsService.save(departmentRequest.name());
    }

    @PostMapping("/delete/{id}")
    public void deleteById (@PathVariable Integer id) {
        departmentsService.deleteById(id);
    }

    @PostMapping("/rename/{id}")
    public void renameById (@PathVariable Integer id, String name) {
        departmentsService.renameById(id, name);
    }

    @GetMapping("/{id}")
    public Optional<Department> getDepartment (@PathVariable Integer id) {
        return departmentsService.findById(id);
    }

    @GetMapping("/api/departments/")
    public List<Department> findAll() {
        return departmentsService.findAll();
    }
}
