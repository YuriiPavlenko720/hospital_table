package lemon.hospitaltable.table;

import lemon.hospitaltable.table.objects.Department;
import lemon.hospitaltable.table.objects.DepartmentRequest;
import lemon.hospitaltable.table.objects.Doctor;
import lemon.hospitaltable.table.objects.Ward;
import lemon.hospitaltable.table.repositories.DepartmentsRepositoryInterface;
import lemon.hospitaltable.table.repositories.DoctorsRepositoryInterface;
import lemon.hospitaltable.table.repositories.WardsRepositoryInterface;
import lemon.hospitaltable.table.services.DepartmentsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentsServiceTest {

    @Mock
    private DepartmentsRepositoryInterface departmentsRepository;

    @Mock
    private DoctorsRepositoryInterface doctorsRepository;

    @Mock
    private WardsRepositoryInterface wardsRepository;

    @InjectMocks
    private DepartmentsService departmentsService;

    @BeforeEach
    void setUp() {

        departmentsService = new DepartmentsService(departmentsRepository, doctorsRepository, wardsRepository);
    }

    @Test
    public void testSaveDepartment() {
        DepartmentRequest request = new DepartmentRequest("testDepartment01");
        Department department = new Department(null, "testDepartment01");
        Department savedDepartment = new Department(1, "testDepartment01");

        when(departmentsRepository.save(department)).thenReturn(savedDepartment);

        Department result = departmentsService.save(request);

        assertEquals(savedDepartment, result);
        verify(departmentsRepository, times(1)).save(department);
    }

    @Test
    public void testDeleteDepartmentById_DepartmentNotFound() {
        Integer departmentId = 1;

        when(departmentsRepository.findById(departmentId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                departmentsService.deleteById(departmentId));

        assertEquals("Department ID " + departmentId + " not found.", exception.getMessage());
        verify(departmentsRepository, times(1)).findById(departmentId);
        verify(departmentsRepository, never()).deleteById(anyInt());
    }

    @Test
    public void testDeleteDepartmentById_WithWards() {
        Integer departmentId = 1;
        Department department = new Department(departmentId, "testDepartment02");

        when(departmentsRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(wardsRepository.findByDepartmentId(departmentId)).thenReturn(List.of(new Ward(
                1,
                0,
                "testWard",
                departmentId,
                5
        )));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                departmentsService.deleteById(departmentId));

        assertTrue(exception.getMessage().contains("Cannot delete department with wards"));
        verify(departmentsRepository, times(1)).findById(departmentId);
        verify(departmentsRepository, never()).deleteById(anyInt());
    }

    @Test
    public void testDeleteDepartmentById_WithDoctors() {
        Integer departmentId = 1;
        Department department = new Department(departmentId, "testDepartment03");

        when(departmentsRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(wardsRepository.findByDepartmentId(departmentId)).thenReturn(Collections.emptyList());
        when(doctorsRepository.findByDepartmentId(departmentId)).thenReturn(List.of(new Doctor(
                1,
                "Dr. Test",
                LocalDate.of(1980, 12, 25),
                "male",
                "Lviv",
                "352253",
                "undefined",
                "test",
                departmentId,
                "drtest@drtest.com"
        )));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                departmentsService.deleteById(departmentId));

        assertTrue(exception.getMessage().contains("Cannot delete department with doctors"));
        verify(departmentsRepository, times(1)).findById(departmentId);
        verify(departmentsRepository, never()).deleteById(anyInt());
    }

    @Test
    public void testDeleteDepartmentById_SuccessfulDeletion() {
        Integer departmentId = 1;
        Department department = new Department(departmentId, "testDepartment04");

        when(departmentsRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(wardsRepository.findByDepartmentId(departmentId)).thenReturn(Collections.emptyList());
        when(doctorsRepository.findByDepartmentId(departmentId)).thenReturn(Collections.emptyList());

        departmentsService.deleteById(departmentId);

        verify(departmentsRepository, times(1)).findById(departmentId);
        verify(departmentsRepository, times(1)).deleteById(departmentId);
    }

    @Test
    public void testRenameDepartmentById_Success() {
        Integer departmentId = 1;
        String newName = "New Department Name";
        Department department = new Department(departmentId, "Old Department Name");

        when(departmentsRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(departmentsRepository.save(department.withName(newName))).thenReturn(department.withName(newName));

        departmentsService.renameById(departmentId, newName);

        verify(departmentsRepository, times(1)).findById(departmentId);
        verify(departmentsRepository, times(1)).save(department.withName(newName));
    }

    @Test
    public void testFindById_DepartmentExists() {
        Integer departmentId = 1;
        Department department = new Department(departmentId, "testDepartment05");

        when(departmentsRepository.findById(departmentId)).thenReturn(Optional.of(department));

        Optional<Department> result = departmentsService.findById(departmentId);

        assertTrue(result.isPresent());
        assertEquals(department, result.get());
        verify(departmentsRepository, times(1)).findById(departmentId);
    }

    @Test
    public void testFindById_DepartmentNotFound() {
        Integer departmentId = 1;

        when(departmentsRepository.findById(departmentId)).thenReturn(Optional.empty());

        Optional<Department> result = departmentsService.findById(departmentId);

        assertFalse(result.isPresent());
        verify(departmentsRepository, times(1)).findById(departmentId);
    }

    @Test
    public void testFindDepartments_WithName() {
        String departmentName = "testDepartment06";
        Department department = new Department(1, departmentName);

        when(departmentsRepository.findByName(departmentName)).thenReturn(List.of(department));

        List<Department> result = departmentsService.findDepartments(departmentName);

        assertEquals(1, result.size());
        assertEquals(department, result.getFirst());
        verify(departmentsRepository, times(1)).findByName(departmentName);
    }

    @Test
    public void testFindDepartments_WithoutName() {
        Department department1 = new Department(1, "testDepartment07");
        Department department2 = new Department(2, "testDepartment08");

        when(departmentsRepository.findAll()).thenReturn(List.of(department1, department2));

        List<Department> result = departmentsService.findDepartments(null);

        assertEquals(2, result.size());
        assertTrue(result.contains(department1));
        assertTrue(result.contains(department2));
        verify(departmentsRepository, times(1)).findAll();
    }
}
