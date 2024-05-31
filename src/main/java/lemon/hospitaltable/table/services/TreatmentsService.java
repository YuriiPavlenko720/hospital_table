package lemon.hospitaltable.table.services;

import lemon.hospitaltable.table.objects.Treatment;
import lemon.hospitaltable.table.repositories.TreatmentsRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TreatmentsService {
    private final TreatmentsRepositoryInterface treatmentsRepository;

    @Autowired
    public TreatmentsService(TreatmentsRepositoryInterface treatmentsRepository) {
        this.treatmentsRepository = treatmentsRepository;
    }

//
//
//    public void save(String name, Date birth, String address, String status, String notation) {
//        Patient patient = new Patient(null, name, birth, address, status, notation);
//        patientsRepository.save(patient);
//    }
//
//    public void deleteById(Long id) {
//        patientsRepository.deleteById(id);
//    }
//
//    public void renameById(Long id, String name) {
//        patientsRepository.renameById(id, name);
//    }
//
//    public void changeBirthById(Long id, Date birth) {
//        patientsRepository.changeBirthById(id, birth);
//    }
//
//    public void changeAddressById(Long id, String address) {
//        patientsRepository.changeAddressById(id, address);
//    }
//
//    public void changeStatusById(Long id, String status) {
//        patientsRepository.changeStatusById(id, status);
//    }
//
//    public void changeNotationById(Long id, String notation) {
//        patientsRepository.changeNotationById(id, notation);
//    }
//
//    public Optional<Patient> findById(Long id) {
//        return patientsRepository.findById(id);
//    }
//
//    public List<Patient> findAll() {
//        return (List<Patient>) patientsRepository.findAll();
//    }
//
//    public List<Patient> findByName(String name) {
//        return patientsRepository.findByName(name);
//    }
//
//    public List<Patient> findByStatus(String status) {
//        return patientsRepository.findByStatus(status);
//    }
}
