package lemon.hospitaltable.table.services;

import lemon.hospitaltable.table.controllers.DoctorsController;
import lemon.hospitaltable.table.objects.Doctor;
import lemon.hospitaltable.table.objects.Treatment;
import lemon.hospitaltable.table.repositories.DoctorsRepositoryInterface;
import lemon.hospitaltable.table.repositories.TreatmentsRepositoryInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class DoctorsService {

    private final DoctorsRepositoryInterface doctorsRepository;
    private final TreatmentsRepositoryInterface treatmentsRepository;


    public void save(DoctorsController.DoctorRequest doctorRequest) {
        doctorsRepository.save(new Doctor(
                null,
                doctorRequest.name(),
                doctorRequest.birth(),
                doctorRequest.position(),
                doctorRequest.departmentId(),
                doctorRequest.email()
        ));
    }


    public void deleteById(Integer id) {
        //checking existence of the aim doctor
        Doctor doctor = doctorsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor ID " + id + " not found."));

        //checking treatments existing at the aim doctor
        List<Treatment> treatments = treatmentsRepository.findByDoctorId(id);
        for (Treatment treatment : treatments) {
            if (treatment.dateOut().isAfter(LocalDate.now())) {
                throw new IllegalArgumentException(
                        "Cannot delete doctor with active or planned treatments at the doctor: \n" + treatment
                );
            }
        }

        //deleting of the aim doctor
        doctorsRepository.deleteById(id);
    }


    public void renameById(Integer id, String newName) {
        Doctor doctor = doctorsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor ID " + id + " not found."));
        doctorsRepository.save(doctor.withName(newName));
    }


    public void changeBirthById(Integer id, LocalDate newBirth) {
        Doctor doctor = doctorsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor ID " + id + " not found."));
        doctorsRepository.save(doctor.withBirth(newBirth));
    }


    public void changePositionById(Integer id, String newPosition) {
        Doctor doctor = doctorsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor ID " + id + " not found."));
        doctorsRepository.save(doctor.withPosition(newPosition));
    }


    public void changeDepartmentById(Integer id, Integer newDepartmentId) {
        //checking existence and getting of the aim doctor
        Doctor doctor = doctorsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor ID " + id + " not found."));

        //checking existence of treatments at the aim doctor
        List<Treatment> treatments = treatmentsRepository.findByDoctorId(id);
        for (Treatment treatment : treatments) {
            if (treatment.dateOut().isAfter(LocalDate.now())) {
                throw new IllegalArgumentException(
                        "Cannot change department of doctor with active or planned treatments at the doctor: \n" +
                                treatment
                );
            }
        }

        //saving doctor with new department
        doctorsRepository.save(doctor.withDepartmentId(newDepartmentId));
    }


    public void changeEmailById(Integer id, String newEmail) {
        Doctor doctor = doctorsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor ID " + id + " not found."));
        doctorsRepository.save(doctor.withEmail(newEmail));
    }


    public Optional<Doctor> findById(Integer id) {
        return doctorsRepository.findById(id);
    }


    public List<Doctor> findAll() {
        return (List<Doctor>) doctorsRepository.findAll();
    }


    public List<Doctor> findByName(String name) {
        return doctorsRepository.findByName(name);
    }


    public List<Doctor> findByDepartmentId(Integer departmentId) {
        return doctorsRepository.findByDepartmentId(departmentId);
    }


    public List<Doctor> findByPosition(String position) {
        return doctorsRepository.findByPosition(position);
    }
}

