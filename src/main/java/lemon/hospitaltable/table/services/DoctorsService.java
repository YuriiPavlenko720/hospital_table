package lemon.hospitaltable.table.services;

import lemon.hospitaltable.table.controllers.DoctorsController;
import lemon.hospitaltable.table.objects.Doctor;
import lemon.hospitaltable.table.objects.Treatment;
import lemon.hospitaltable.table.repositories.DoctorsRepositoryInterface;
import lemon.hospitaltable.table.repositories.TreatmentsRepositoryInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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


    public List<Doctor> findDoctors(Integer id, String name, Integer departmentId, String position) {
        if (id != null) {
            Optional<Doctor> doctor = doctorsRepository.findById(id);
            return doctor.map(Collections::singletonList)
                    .orElse(Collections.emptyList());
        } else if (name != null && !name.isEmpty()) {
            return doctorsRepository.findByName(name);
        } else if (departmentId != null) {
            return doctorsRepository.findByDepartmentId(departmentId);
        } else if (position != null && !position.isEmpty()) {
            return doctorsRepository.findByPosition(position);
        } else {
            return StreamSupport.stream(doctorsRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());
        }
    }
}

