package lemon.hospitaltable.table.controllers;

import lemon.hospitaltable.table.objects.Department;
import lemon.hospitaltable.table.services.DepartmentsService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class DepartmentsController {

    private final DepartmentsService departmentsService;

    public DepartmentsController(DepartmentsService departmentsService) {
        this.departmentsService = departmentsService;
    }

    @PostMapping("/api/departments")
    public void createDepartment (@RequestBody Department department) {
        departmentsService.save(department);
    }

    //deleteById

    @GetMapping("/api/departments/{id}")
    public Department getDepartment (@PathVariable int id) {
        return departmentsService.findById(id);
    }

    @GetMapping("/api/departments/")
    public List<Department> findAll() {
        return departmentsService.findAll();
    }

    //setDepartmentName
}
