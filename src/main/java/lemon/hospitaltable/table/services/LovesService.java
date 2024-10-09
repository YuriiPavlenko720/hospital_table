package lemon.hospitaltable.table.services;

import lemon.hospitaltable.table.objects.*;
import lemon.hospitaltable.table.objects.loveResponses.AverageRatingResponse;
import lemon.hospitaltable.table.objects.loveResponses.DoctorRatingResponse;
import lemon.hospitaltable.table.objects.loveResponses.PatientRatingResponse;
import lemon.hospitaltable.table.repositories.LovesRepositoryInterface;
import lemon.hospitaltable.table.repositories.PatientsRepositoryInterface;
import lemon.hospitaltable.table.repositories.TreatmentsRepositoryInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@AllArgsConstructor
@Service
public class LovesService {

    private final LovesRepositoryInterface lovesRepository;
    private final PatientsRepositoryInterface patientsRepository;
    private final TreatmentsRepositoryInterface treatmentsRepository;


    public Love save(LoveRequest loveRequest, @AuthenticationPrincipal OidcUser principal) {

        //getting of the email of the author
        String userEmail = principal.getEmail();

        //getting the patient by email
        Patient patient = patientsRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Patient with email " + userEmail + " not found."));

        //creating love for validation
        Love love = new Love(
                null,
                LocalDate.now(),
                patient.id(),
                loveRequest.doctorId(),
                loveRequest.rating(),
                loveRequest.comment()
        );

        //checking if treatments with patient and doctor exist
        Optional<Treatment> existingTreatment = treatmentsRepository.findByPatientIdAndDoctorId(
                patient.id(), loveRequest.doctorId()
        );
        if (existingTreatment.isEmpty()) {
            throw new IllegalArgumentException(
                    "A patient ID " + patient.id() + " was not treating by doctor ID " + loveRequest.doctorId() + "."
            );
        }

        //checking if there is another love with patient and doctor
        Optional<Love> existingLove = lovesRepository.findByPatientIdAndDoctorId(patient.id(), loveRequest.doctorId());
        if (existingLove.isPresent()) {
            throw new IllegalArgumentException(
                    "A patient ID " + patient.id() + " has already loved doctor ID " + loveRequest.doctorId() + "."
            );
        }

        //saving love
        return lovesRepository.save(love);
    }


    public void deleteById(Long id) {
        //checking existence of the aim love
        Love love = lovesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Love ID " + id + " not found."));

        //deleting of the love
        lovesRepository.deleteById(id);
    }


    public Love changeLove(Long loveId, LoveRequest updatedLoveRequest, @AuthenticationPrincipal OidcUser principal) {

        //getting of the email of the author
        String userEmail = principal.getEmail();

        //getting the patient by email
        Patient patient = patientsRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Patient with email " + userEmail + " not found."));

        //getting the existing love
        Love existingLove = lovesRepository.findById(loveId)
                .orElseThrow(() -> new IllegalArgumentException("Love ID " + loveId + " not found."));

        //checking if the current user is the owner of the love entry
        if (!existingLove.patientId().equals(patient.id())) {
            throw new IllegalArgumentException("You do not have permission to update this love rating.");
        }

        //update the existing love with new details
        Love updatedLove = existingLove.withRating(updatedLoveRequest.rating())
                .withComment(updatedLoveRequest.comment())
                .withDate(LocalDate.now()); //update the date to the current one

        return lovesRepository.save(updatedLove);
    }


    public List<Love> getLovesByPatient(@AuthenticationPrincipal OidcUser principal) {
        String userEmail = principal.getEmail();
        Patient patient = patientsRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Patient with email " + userEmail + " not found."));

        return lovesRepository.findByPatientId(patient.id());
    }


    public AverageRatingResponse getAverageRatingByDoctorId(Integer doctorId) {
        List<Love> loves = lovesRepository.findByDoctorId(doctorId);
        if (loves.isEmpty()) {
            throw new IllegalArgumentException("No ratings found for doctor ID " + doctorId);
        }

        double averageRating = loves.stream()
                .mapToInt(Love::rating)
                .average()
                .orElse(0.0);

        return new AverageRatingResponse(averageRating, loves);
    }

    public List<DoctorRatingResponse> getTopAndBottomDoctors() {
        List<DoctorRatingResponse> topDoctors = lovesRepository.findTopDoctors();
        List<DoctorRatingResponse> bottomDoctors = lovesRepository.findBottomDoctors();

        List<DoctorRatingResponse> result = new ArrayList<>();
        result.addAll(topDoctors);
        result.addAll(bottomDoctors);
        return result;
    }

    public List<PatientRatingResponse> getTopPatients() {
        return lovesRepository.findTopPatients();
    }
}
