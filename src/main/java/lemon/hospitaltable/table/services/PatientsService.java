package lemon.hospitaltable.table.services;

import lemon.hospitaltable.table.controllers.PatientsController;
import lemon.hospitaltable.table.objects.Patient;
import lemon.hospitaltable.table.objects.Treatment;
import lemon.hospitaltable.table.repositories.PatientsRepositoryInterface;
import lemon.hospitaltable.table.repositories.TreatmentsRepositoryInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PatientsService {

    private final PatientsRepositoryInterface patientsRepository;
    private final TreatmentsRepositoryInterface treatmentsRepository;


    public void save(PatientsController.PatientRequest patientRequest) {
        patientsRepository.save(new Patient(
                null,
                patientRequest.name(),
                patientRequest.birth(),
                patientRequest.address(),
                patientRequest.email(),
                patientRequest.status(),
                patientRequest.notation()
        ));
    }


    public void deleteById(Long id) {
        //checking existence of the aim patient
        Patient patient = patientsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient ID " + id + " not found."));

        //checking existence of treatments with the aim patient
        List<Treatment> treatments = treatmentsRepository.findByPatientId(id);
        for (Treatment treatment : treatments) {
            if (treatment.dateOut().isAfter(LocalDate.now())) {
                throw new IllegalArgumentException(
                        "Cannot delete patient with active or planned treatments \n" +
                                treatments
                );
            }
        }

        //deleting of the aim patient
        patientsRepository.deleteById(id);
    }


    public void renameById(Long id, String newName) {
        Patient patient = patientsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient ID " + id + " not found."));
        patientsRepository.save(patient.withName(newName));
    }


    public void changeBirthById(Long id, LocalDate newBirth) {
        Patient patient = patientsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient ID " + id + " not found."));
        patientsRepository.save(patient.withBirth(newBirth));
    }


    public void changeAddressById(Long id, String newAddress) {
        Patient patient = patientsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient ID " + id + " not found."));
        patientsRepository.save(patient.withAddress(newAddress));
    }


    public void changeEmailById(Long id, String newEmail) {
        Patient patient = patientsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient ID " + id + " not found."));
        patientsRepository.save(patient.withEmail(newEmail));
    }


    public void changeStatusById(Long id, String newStatus) {
        Patient patient = patientsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient ID " + id + " not found."));
        patientsRepository.save(patient.withStatus(newStatus));
    }


    public void changeNotationById(Long id, String newNotation) {
        Patient patient = patientsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient ID " + id + " not found."));
        patientsRepository.save(patient.withNotation(newNotation));
    }


    public Optional<Patient> findById(Long id) {
        return patientsRepository.findById(id);
    }


    public List<Patient> findAll() {
        return (List<Patient>) patientsRepository.findAll();
    }


    public List<Patient> findByName(String name) {
        return patientsRepository.findByName(name);
    }


    public List<Patient> findByStatus(String status) {
        return patientsRepository.findByStatus(status);
    }
}
