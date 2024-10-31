package lemon.hospitaltable.table;

import lemon.hospitaltable.table.objects.Love;
import lemon.hospitaltable.table.objects.LoveRequest;
import lemon.hospitaltable.table.objects.Patient;
import lemon.hospitaltable.table.objects.Treatment;
import lemon.hospitaltable.table.objects.loveResponses.AverageRatingResponse;
import lemon.hospitaltable.table.objects.loveResponses.DoctorRatingResponse;
import lemon.hospitaltable.table.objects.loveResponses.PatientRatingResponse;
import lemon.hospitaltable.table.repositories.LovesRepositoryInterface;
import lemon.hospitaltable.table.repositories.PatientsRepositoryInterface;
import lemon.hospitaltable.table.repositories.TreatmentsRepositoryInterface;
import lemon.hospitaltable.table.services.LovesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LovesServiceTest {
    @Mock
    private LovesRepositoryInterface lovesRepository;

    @Mock
    private PatientsRepositoryInterface patientsRepository;

    @Mock
    private TreatmentsRepositoryInterface treatmentsRepository;

    @Mock
    private OidcUser principal;

    @InjectMocks
    private LovesService lovesService;

    private Patient patient;
    private Love love;
    private LoveRequest loveRequest;

    @BeforeEach
    void setUp() {
        patient = new Patient(
                1L,
                "John Doe",
                LocalDate.of(1980, 12, 25),
                "male",
                "355-255",
                "undefined",
                "Lviv",
                "johndoe@drtest.com",
                "test patient",
                "test"
        );
        loveRequest = new LoveRequest(
                2,
                (byte) 2,
                "Great doctor");
        love = new Love(
                1L,
                LocalDate.now(),
                patient.id(),
                loveRequest.doctorId(),
                loveRequest.rating(),
                loveRequest.comment());
    }

    @Test
    void testSaveLove_Success() {
        when(principal.getEmail()).thenReturn("johndoe@drtest.com");
        when(patientsRepository.findByEmail("johndoe@drtest.com")).thenReturn(Optional.of(patient));
        when(treatmentsRepository.findByPatientIdAndDoctorId(patient.id(), loveRequest.doctorId())).thenReturn(Optional.of(mock(Treatment.class)));
        when(lovesRepository.findByPatientIdAndDoctorId(patient.id(), loveRequest.doctorId())).thenReturn(Optional.empty());
        when(lovesRepository.save(any(Love.class))).thenReturn(love);

        Love savedLove = lovesService.save(loveRequest, principal);

        assertEquals(love.comment(), savedLove.comment());
        verify(lovesRepository).save(any(Love.class));
    }

    @Test
    void testDeleteById_Success() {
        when(lovesRepository.findById(1L)).thenReturn(Optional.of(love));

        lovesService.deleteById(1L);

        verify(lovesRepository).deleteById(1L);
    }

    @Test
    void testChangeLove_Success() {
        when(principal.getEmail()).thenReturn("johndoe@drtest.com");
        when(patientsRepository.findByEmail("johndoe@drtest.com")).thenReturn(Optional.of(patient));
        when(lovesRepository.findById(1L)).thenReturn(Optional.of(love));

        LoveRequest updatedLoveRequest = new LoveRequest(1, (byte) 1, "Updated comment");
        Love updatedLove = new Love(
                love.id(),
                LocalDate.now(),
                love.patientId(),
                love.doctorId(),
                updatedLoveRequest.rating(),
                updatedLoveRequest.comment()
        );

        when(lovesRepository.save(any(Love.class))).thenReturn(updatedLove);

        Love result = lovesService.changeLove(1L, updatedLoveRequest, principal);

        assertNotNull(result);
        assertEquals(updatedLove.comment(), result.comment());
        assertEquals(updatedLove.rating(), result.rating());
        assertEquals(updatedLove.doctorId(), result.doctorId());
    }

    @Test
    void testGetLovesByPatient_Success() {
        when(principal.getEmail()).thenReturn("johndoe@drtest.com");
        when(patientsRepository.findByEmail("johndoe@drtest.com")).thenReturn(Optional.of(patient));
        when(lovesRepository.findByPatientId(patient.id())).thenReturn(List.of(love));

        List<Love> loves = lovesService.getLovesByPatient(principal);

        assertEquals(1, loves.size());
        assertEquals("Great doctor", loves.getFirst().comment());
        verify(lovesRepository).findByPatientId(patient.id());
    }

    @Test
    void testGetAverageRatingByDoctorId() {
        when(lovesRepository.findByDoctorId(2)).thenReturn(List.of(love));

        AverageRatingResponse response = lovesService.getAverageRatingByDoctorId(2);

        assertEquals(2.0, response.averageRating());
        assertEquals(1, response.comments().size());
        verify(lovesRepository).findByDoctorId(2);
    }

    @Test
    void testGetTopAndBottomDoctors() {
        DoctorRatingResponse topDoctor = new DoctorRatingResponse(1L, 1.8);
        DoctorRatingResponse bottomDoctor = new DoctorRatingResponse(2L, 1.1);

        when(lovesRepository.findTopDoctors()).thenReturn(List.of(topDoctor));
        when(lovesRepository.findBottomDoctors()).thenReturn(List.of(bottomDoctor));

        List<DoctorRatingResponse> result = lovesService.getTopAndBottomDoctors();

        assertEquals(2, result.size());
        assertTrue(result.contains(topDoctor));
        assertTrue(result.contains(bottomDoctor));
        verify(lovesRepository).findTopDoctors();
        verify(lovesRepository).findBottomDoctors();
    }

    @Test
    void testGetTopPatients() {
        PatientRatingResponse topPatient = new PatientRatingResponse(1L, 5);

        when(lovesRepository.findTopPatients()).thenReturn(List.of(topPatient));

        List<PatientRatingResponse> result = lovesService.getTopPatients();

        assertEquals(1, result.size());
        assertEquals(1, result.getFirst().patientId());
        verify(lovesRepository).findTopPatients();
    }
}
