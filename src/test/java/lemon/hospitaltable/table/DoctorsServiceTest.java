package lemon.hospitaltable.table;

import lemon.hospitaltable.table.objects.Department;
import lemon.hospitaltable.table.objects.Doctor;
import lemon.hospitaltable.table.objects.DoctorRequest;
import lemon.hospitaltable.table.objects.Treatment;
import lemon.hospitaltable.table.repositories.DepartmentsRepositoryInterface;
import lemon.hospitaltable.table.repositories.DoctorsRepositoryInterface;
import lemon.hospitaltable.table.repositories.TreatmentsRepositoryInterface;
import lemon.hospitaltable.table.services.DoctorsService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorsServiceTest {

    @Mock
    private DoctorsRepositoryInterface doctorsRepository;

    @Mock
    private TreatmentsRepositoryInterface treatmentsRepository;

    @Mock
    private DepartmentsRepositoryInterface departmentsRepository;

    @InjectMocks
    private DoctorsService doctorsService;

    private DoctorRequest doctorRequest;
    private Doctor doctor;
    private Department department;

    @BeforeEach
    void setUp() {

        department = new Department(1, "Cardiology");

        doctorRequest = new DoctorRequest(
                "Dr. Test",
                LocalDate.of(1980, 12, 25),
                "male",
                "Lviv",
                "352253",
                "undefined",
                "test",
                1,
                "drtest@drtest.com"
        );

        doctor = new Doctor(
                1,
                "Dr. Test",
                LocalDate.of(1980, 12, 25),
                "male",
                "Lviv",
                "352253",
                "undefined",
                "test",
                1,
                "drtest@drtest.com"
        );
    }

    @Test
    void testSaveDoctor_Success() {
        when(departmentsRepository.findById(doctorRequest.departmentId())).thenReturn(Optional.of(department));
        when(doctorsRepository.save(any(Doctor.class))).thenReturn(doctor);

        Doctor savedDoctor = doctorsService.save(doctorRequest);

        assertNotNull(savedDoctor);
        assertEquals(doctor.name(), savedDoctor.name());
        verify(departmentsRepository).findById(doctorRequest.departmentId());
        verify(doctorsRepository).save(any(Doctor.class));
    }

    @Test
    void testSaveDoctor_DepartmentNotFound() {
        when(departmentsRepository.findById(doctorRequest.departmentId())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> doctorsService.save(doctorRequest));
        verify(departmentsRepository).findById(doctorRequest.departmentId());
        verifyNoMoreInteractions(doctorsRepository);
    }

    @Test
    void testDeleteDoctor_Success() {
        when(doctorsRepository.findById(doctor.id())).thenReturn(Optional.of(doctor));
        when(treatmentsRepository.findByDoctorId(doctor.id())).thenReturn(Collections.emptyList());

        doctorsService.deleteById(doctor.id());

        verify(doctorsRepository).findById(doctor.id());
        verify(treatmentsRepository).findByDoctorId(doctor.id());
        verify(doctorsRepository).deleteById(doctor.id());
    }

    @Test
    void testDeleteDoctor_DoctorNotFound() {
        when(doctorsRepository.findById(doctor.id())).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> doctorsService.deleteById(doctor.id()));
        verify(doctorsRepository).findById(doctor.id());
        verifyNoMoreInteractions(treatmentsRepository);
    }

    @Test
    void testDeleteDoctor_HasActiveTreatments() {
        when(doctorsRepository.findById(doctor.id())).thenReturn(Optional.of(doctor));
        Treatment activeTreatment = new Treatment(
                1L,
                1L,
                doctor.id(),
                1,
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(5),
                "Test Treatment",
                "test"
        );
        when(treatmentsRepository.findByDoctorId(doctor.id())).thenReturn(List.of(activeTreatment));

        assertThrows(IllegalArgumentException.class, () -> doctorsService.deleteById(doctor.id()));
        verify(treatmentsRepository).findByDoctorId(doctor.id());
    }

    @Test
    void testRenameDoctor_Success() {
        when(doctorsRepository.findById(doctor.id())).thenReturn(Optional.of(doctor));

        doctorsService.renameById(doctor.id(), "Dr. NewName");

        verify(doctorsRepository).findById(doctor.id());
        verify(doctorsRepository).save(doctor.withName("Dr. NewName"));
    }

    @Test
    void testChangeBirthById_Success() {
        when(doctorsRepository.findById(doctor.id())).thenReturn(Optional.of(doctor));

        doctorsService.changeBirthById(doctor.id(), LocalDate.of(1975, 5, 5));

        verify(doctorsRepository).findById(doctor.id());
        verify(doctorsRepository).save(doctor.withBirth(LocalDate.of(1975, 5, 5)));
    }

    @Test
    void testChangeDepartmentById_Success() {
        when(doctorsRepository.findById(doctor.id())).thenReturn(Optional.of(doctor));
        when(departmentsRepository.findById(department.id())).thenReturn(Optional.of(department));
        when(treatmentsRepository.findByDoctorId(doctor.id())).thenReturn(Collections.emptyList());

        doctorsService.changeDepartmentById(doctor.id(), department.id());

        verify(doctorsRepository).findById(doctor.id());
        verify(departmentsRepository).findById(department.id());
        verify(doctorsRepository).save(doctor.withDepartmentId(department.id()));
    }

    @Test
    void testChangeEmailById_Success() {
        when(doctorsRepository.findById(doctor.id())).thenReturn(Optional.of(doctor));

        doctorsService.changeEmailById(doctor.id(), "new.email@example.com");

        verify(doctorsRepository).findById(doctor.id());
        verify(doctorsRepository).save(doctor.withEmail("new.email@example.com"));
    }

    @Test
    void testFindById() {
        when(doctorsRepository.findById(doctor.id())).thenReturn(Optional.of(doctor));

        Optional<Doctor> foundDoctor = doctorsService.findById(doctor.id());

        assertTrue(foundDoctor.isPresent());
        assertEquals(doctor.name(), foundDoctor.get().name());
        verify(doctorsRepository).findById(doctor.id());
    }

    @Test
    void testFindDoctors_ByName() {
        when(doctorsRepository.findByName("Dr. Smith")).thenReturn(List.of(doctor));

        List<Doctor> doctors = doctorsService.findDoctors("Dr. Smith", null, null);

        assertEquals(1, doctors.size());
        verify(doctorsRepository).findByName("Dr. Smith");
    }

    @Test
    void testFindDoctors_All() {
        when(doctorsRepository.findAll()).thenReturn(List.of(doctor));

        List<Doctor> doctors = doctorsService.findDoctors(null, null, null);

        assertEquals(1, doctors.size());
        verify(doctorsRepository).findAll();
    }

    @Test
    void testGetDoctorsOccupancyStats() {
        when(doctorsRepository.findAll()).thenReturn(List.of(doctor));
        when(departmentsRepository.findAll()).thenReturn(List.of(department));
        when(treatmentsRepository.countTreatmentsByDateAndDoctor(any(LocalDate.class), eq(doctor.id()))).thenReturn(3L);

        String stats = doctorsService.getDoctorsOccupancyStats(LocalDate.now().minusDays(7), LocalDate.now());

        assertNotNull(stats);
        verify(doctorsRepository).findAll();
        verify(departmentsRepository).findAll();
    }
}
