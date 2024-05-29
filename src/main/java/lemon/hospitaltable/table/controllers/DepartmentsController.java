package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.objects.Department;
import lemon.hospitaltable.table.objects.DepartmentRequest;
import lemon.hospitaltable.table.services.DepartmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class DepartmentsController {

    private final DepartmentsService departmentsService;

    @Autowired
    public DepartmentsController(DepartmentsService departmentsService) {
        this.departmentsService = departmentsService;
    }

    @PostMapping("/api/departments")
    public void createDepartment (@RequestBody DepartmentRequest departmentRequest) {
        departmentsService.save(departmentRequest.getName());
    }

    @PostMapping("/api/departments/delete/{id}")
    public void deleteById (@PathVariable Integer id) {
        departmentsService.deleteById(id);
    }

    @PostMapping("/api/departments/rename/{id}")
    public void renameById (@PathVariable Integer id, String name) {
        departmentsService.renameById(id, name);
    }

    @GetMapping("/api/departments/{id}")
    public Optional<Department> getDepartment (@PathVariable Integer id) {
        return departmentsService.findById(id);
    }

    @GetMapping("/api/departments/")
    public List<Department> findAll() {
        return departmentsService.findAll();
    }
}
