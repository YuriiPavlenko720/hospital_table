package lemon.hospitaltable.table.services;

import lemon.hospitaltable.table.objects.*;
import lemon.hospitaltable.table.repositories.DoctorsRepositoryInterface;
import lemon.hospitaltable.table.repositories.PatientsRepositoryInterface;
import lemon.hospitaltable.table.repositories.TreatmentsRepositoryInterface;
import lemon.hospitaltable.table.repositories.WardsRepositoryInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class TreatmentsService {

    private final TreatmentsRepositoryInterface treatmentsRepository;
    private final PatientsRepositoryInterface patientsRepository;
    private final DoctorsRepositoryInterface doctorsRepository;
    private final WardsRepositoryInterface wardsRepository;
    private final NotificationService notificationService;


    public Treatment save(TreatmentRequest treatmentRequest) {

        //creating treatment for validation
        Treatment treatment = new Treatment(
                null,
                treatmentRequest.patientId(),
                treatmentRequest.doctorId(),
                treatmentRequest.wardId(),
                treatmentRequest.dateIn(),
                treatmentRequest.dateOut(),
                treatmentRequest.diagnosis(),
                treatmentRequest.notation()
        );

        //treatment validation
        validateTreatment(treatment, 0L);

        //checking capacity of new ward
        checkCapacity(treatment, treatment.dateIn(), treatment.dateOut());

        //doctor notification about new treatment
        Patient patient = patientsRepository.findById(treatment.patientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient ID " + treatment.patientId() + " not found."));
        Ward ward = wardsRepository.findById(treatment.wardId())
                .orElseThrow(() -> new IllegalArgumentException("Ward ID " + treatment.wardId() + " not found."));
        notificationService.sendEmail(
                treatment,
                "New treatment.",
                "A new treatment has been created:" +
                        "\nPatient: " + patient + "." +
                        "\nWard: " + ward.name() + "." +
                        "\nDates: " + treatmentRequest.dateIn() + " - " + treatmentRequest.dateOut() + "."
        );

        //saving treatment
        return treatmentsRepository.save(treatment);
    }


    public void deleteById(Long id) {
        //checking existence of the aim treatment
        Treatment treatment = treatmentsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Treatment ID " + id + " not found."));

        //doctor notification about treatment deleting
        notificationService.sendEmail(
                treatment,
                "Treatment deleted.",
                "The treatment ID " + id + " has been deleted."
        );

        //deleting of the treatment
        treatmentsRepository.deleteById(id);
    }


    public void changePatientIdById(Long id, Long newPatientId) {
        //existence and getting the aim treatment
        Treatment treatment = treatmentsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Treatment ID " + id + " not found."));

        //checking new patient existence
        Patient patient = patientsRepository.findById(newPatientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient ID " + newPatientId + " not found."));

        //saving treatment with new patient
        treatmentsRepository.save(treatment.withPatientId(newPatientId));

        //doctor notification about patient changing
        notificationService.sendEmail(
                treatment,
                "Treatment changed.",
                "The treatment ID " + id + " has been changed." +
                        "\nNew patient: " + patient + "."
        );
    }


    public void changeDoctorIdById(Long id, Integer newDoctorId) {
        //checking existence and getting the aim treatment
        Treatment treatment = treatmentsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Treatment ID " + id + " not found."));

        //checking new doctor existence
        Doctor doctor = doctorsRepository.findById(newDoctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor ID " + newDoctorId + " not found."));

        //previous doctor notification about changing
        notificationService.sendEmail(
                treatment,
                "Treatment changed.",
                "The doctor of treatment ID " + id + " has been changed." +
                        "\nYou are not in charge now for this treatment."
        );

        //saving treatment with new doctor
        treatmentsRepository.save(treatment.withDoctorId(newDoctorId));

        //new doctor notification about changing
        Patient patient = patientsRepository.findById(treatment.patientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient ID " + treatment.patientId() + " not found."));
        Ward ward = wardsRepository.findById(treatment.wardId())
                .orElseThrow(() -> new IllegalArgumentException("Ward ID " + treatment.wardId() + " not found."));
        notificationService.sendEmail(
                treatment.withDoctorId(newDoctorId),
                "Treatment changed.",
                "The doctor of treatment ID " + id + " has been changed." +
                        "\nNow you are in charge for this treatment:" +
                        "\nPatient: " + patient + "." +
                        "\nWard: " + ward.name() + "." +
                        "\nDates: " + treatment.dateIn() + " - " + treatment.dateOut() + "."
        );
    }


    public void changeWardIdById(Long id, Integer newWardId) {
        //checking existence and getting the aim treatment
        Treatment treatment = treatmentsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Treatment ID " + id + " not found."));

        //checking new ward existence
        Ward ward = wardsRepository.findById(newWardId)
                .orElseThrow(() -> new IllegalArgumentException("Ward ID " + newWardId + " not found."));

        //checking capacity of new ward
        checkCapacity(treatment, treatment.dateIn(), treatment.dateOut());

        //saving treatment with new ward
        treatmentsRepository.save(treatment.withWardId(newWardId));

        //doctor notification about ward changing
        notificationService.sendEmail(
                treatment,
                "Treatment changed.",
                "The treatment ID " + id + " has been changed." +
                        "\nNew ward: " + ward.name() + "."
        );
    }


    public void changeDateInById(Long id, LocalDate newDateIn) {
        //checking existence and getting the aim treatment
        Treatment treatment = treatmentsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Treatment ID " + id + " not found."));

        //treatment validation
        validateTreatment(treatment.withDateIn(newDateIn), treatment.id());

        //checking capacity of ward
        checkCapacity(treatment, newDateIn, treatment.dateIn().minusDays(1));

        //saving treatment with new dateIn
        treatmentsRepository.save(treatment.withDateIn(newDateIn));

        //doctor notification about date changing
        notificationService.sendEmail(
                treatment,
                "Treatment changed.",
                "The treatment ID " + id + " has been changed." +
                        "\nNew dateIn: " + newDateIn + "."
        );
    }


    public void changeDateOutById(Long id, LocalDate newDateOut) {
        //checking existence and getting the aim treatment
        Treatment treatment = treatmentsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Treatment ID \" + id + \" not found."));

        //treatment validation
        validateTreatment(treatment.withDateOut(newDateOut), treatment.id());

        //checking capacity of ward
        checkCapacity(treatment, treatment.dateOut().plusDays(1), newDateOut);

        //saving treatment with new dateOut
        treatmentsRepository.save(treatment.withDateOut(newDateOut));

        //doctor notification about date changing
        notificationService.sendEmail(
                treatment,
                "Treatment changed.",
                "The treatment ID " + id + " has been changed." +
                        "\nNew dateOut: " + newDateOut + "."
        );
    }

    private void validateTreatment(Treatment treatment, Long existingTreatmentId) throws IllegalArgumentException {

        //checking patient existence of the aim treatment
        Patient patient = patientsRepository.findById(treatment.patientId())
                .orElseThrow(() -> new IllegalArgumentException("Patient ID " + treatment.patientId() + " not found."));

        //checking order of dates
        LocalDate startDate = treatment.dateIn();
        LocalDate endDate = treatment.dateOut();
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException(
                    "End treating date (" + endDate + ") cannot be before " +
                            "start treating date (" + startDate + ")."
            );
        }

        //checking department of ward and doctor
        Integer departmentIdOfWard = wardsRepository.findById(treatment.wardId())
                .orElseThrow(() -> new IllegalArgumentException("Ward ID " + treatment.wardId() + " not found."))
                .departmentId();
        Integer departmentIdOfDoctor = doctorsRepository.findById(treatment.doctorId())
                .orElseThrow(() -> new IllegalArgumentException("Doctor ID " + treatment.doctorId() + " not found."))
                .departmentId();
        if (!Objects.equals(departmentIdOfDoctor, departmentIdOfWard)) {
            throw new IllegalArgumentException(
                    "The doctor or ward does not belong to the relevant department."
            );
        }

        //checking overtreatment
        List<Treatment> overlappingTreatments = treatmentsRepository.findOvertreatmentsByPatientId(
                treatment.patientId(),
                startDate,
                endDate,
                existingTreatmentId
        );
        if (!overlappingTreatments.isEmpty()) {
            throw new IllegalArgumentException(
                    "Patient ID " + treatment.patientId() + " have already treatment at these dates: "
                            + overlappingTreatments
            );
        }
    }


    private void checkCapacity(Treatment treatment, LocalDate startDate, LocalDate endDate) {
        //getting capacity of ward
        Integer capacity = wardsRepository.getCapacityById(treatment.wardId());
        if (capacity == null) {
            throw new IllegalArgumentException("Ward ID " + treatment.wardId() + " not found.");
        }

        //checking taken less than capacity on each day of planned treatment
        for (
                LocalDate date = startDate;
                !date.isAfter(endDate);
                date = date.plusDays(1)
        ) {
            int treatmentsCount = treatmentsRepository.countTreatmentsInWardOnDate(treatment.wardId(), date);
            if (treatmentsCount >= capacity) {
                throw new IllegalArgumentException("Ward ID " + treatment.wardId() +
                        " capacity exceeded on date: " + date);
            }
        }
    }

    public void changeDiagnosisById(Long id, String newDiagnosis) {
        //checking existence and getting the aim treatment
        Treatment treatment = treatmentsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Treatment ID " + id + " not found."));

        //saving treatment with new diagnosis
        treatmentsRepository.save(treatment.withDiagnosis(newDiagnosis));

        //doctor notification about diagnosis changing
        notificationService.sendEmail(
                treatment,
                "Treatment changed.",
                "The treatment ID " + id + " has been changed." +
                        "\nNew diagnosis: " + newDiagnosis + "."
        );
    }

    public void changeNotationById(Long id, String newNotation) {
        //checking existence and getting the aim treatment
        Treatment treatment = treatmentsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Treatment ID " + id + " not found."));

        //saving treatment with new notation
        treatmentsRepository.save(treatment.withNotation(newNotation));

        //doctor notification about notation changing
        notificationService.sendEmail(
                treatment,
                "Treatment changed.",
                "The treatment ID " + id + " has been changed." +
                        "\nNew notation: " + newNotation + "."
        );
    }

    @Transactional
    public List<TreatmentStats> getTreatmentsStatsByDepartments(
            LocalDate startDate,
            LocalDate endDate
    ) {
        return treatmentsRepository.findAllTreatmentsEndingBetweenByDepartments(startDate, endDate);
    }


    @Transactional
    public Integer countTreatmentsStats(
            LocalDate startDate,
            LocalDate endDate
    ) {
        return treatmentsRepository.countTreatmentsEndingBetween(startDate, endDate);
    }


    @Transactional
    public Integer countTreatmentsStatsByDoctorId(
            Integer doctorId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return treatmentsRepository.countTreatmentsEndingBetweenByDoctorId(
                doctorId,
                startDate,
                endDate
        );
    }


    @Transactional
    public Integer countTreatmentsStatsByDepartmentId(
            Integer departmentId,
            LocalDate startDate,
            LocalDate endDate
    ) {
        return treatmentsRepository.countTreatmentsEndingBetweenByDepartmentId(
                departmentId,
                startDate,
                endDate
        );
    }


    public Optional<Treatment> findById(Long id) {
        return treatmentsRepository.findById(id);
    }


    public List<Treatment> findTreatments(Long patientId, Integer doctorId, Integer wardId) {
        if (patientId != null) {
            return treatmentsRepository.findByPatientId(patientId);
        } else if (doctorId != null) {
            return treatmentsRepository.findByDoctorId(doctorId);
        } else if (wardId != null) {
            return treatmentsRepository.findByWardId(wardId);
        } else {
            return StreamSupport.stream(treatmentsRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());
        }
    }
}
