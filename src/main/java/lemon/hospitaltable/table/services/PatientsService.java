package lemon.hospitaltable.table.services;

import lemon.hospitaltable.table.objects.Patient;
import lemon.hospitaltable.table.objects.PatientRequest;
import lemon.hospitaltable.table.objects.Treatment;
import lemon.hospitaltable.table.repositories.PatientsRepositoryInterface;
import lemon.hospitaltable.table.repositories.TreatmentsRepositoryInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class PatientsService {

    private final PatientsRepositoryInterface patientsRepository;
    private final TreatmentsRepositoryInterface treatmentsRepository;

    public void save(PatientRequest patientRequest) {
        patientsRepository.save(
                new Patient(
                        null,
                        patientRequest.getName(),
                        patientRequest.getBirth(),
                        patientRequest.getAddress(),
                        patientRequest.getStatus(),
                        patientRequest.getNotation()
                )
        );
    }

    public void deleteById(Long id) {
        //checking existing of the aim patient
        patientsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found."));

        //checking treatments existing with the aim patient
        List<Treatment> treatments = treatmentsRepository.findByPatientId(id);
        for (Treatment treatment : treatments) {
            if (treatment.dateOut().after(new Date(System.currentTimeMillis()))) {
                throw new IllegalArgumentException("Cannot delete patient with active or planned treatments.");
            }
        }

        //deleting of the aim patient
        patientsRepository.deleteById(id);
    }

    public void renameById(Long id, String name) {
        patientsRepository.renameById(id, name);
    }

    public void changeBirthById(Long id, Date birth) {
        patientsRepository.changeBirthById(id, birth);
    }

    public void changeAddressById(Long id, String address) {
        patientsRepository.changeAddressById(id, address);
    }

    public void changeStatusById(Long id, String status) {
        patientsRepository.changeStatusById(id, status);
    }

    public void changeNotationById(Long id, String notation) {
        patientsRepository.changeNotationById(id, notation);
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
