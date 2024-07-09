package lemon.hospitaltable.table.services;

import lemon.hospitaltable.table.objects.*;
import lemon.hospitaltable.table.repositories.DepartmentsRepositoryInterface;
import lemon.hospitaltable.table.repositories.DoctorsRepositoryInterface;
import lemon.hospitaltable.table.repositories.TreatmentsRepositoryInterface;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class DoctorsService {

    private final DoctorsRepositoryInterface doctorsRepository;
    private final TreatmentsRepositoryInterface treatmentsRepository;
    private final DepartmentsRepositoryInterface departmentsRepository;


    public Doctor save(DoctorRequest doctorRequest) {

        //checking department existence
        Department department = departmentsRepository.findById(doctorRequest.departmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department ID " + doctorRequest.departmentId() + " not found."));

        //creating of the doctor
        return doctorsRepository.save(new Doctor(
                null,
                doctorRequest.name(),
                doctorRequest.birth(),
                doctorRequest.sex(),
                doctorRequest.address(),
                doctorRequest.phone(),
                doctorRequest.interests(),
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

        //checking new department existence
        Department department = departmentsRepository.findById(newDepartmentId)
                .orElseThrow(() -> new IllegalArgumentException("Department ID " + newDepartmentId + " not found."));

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


    public List<Doctor> findDoctors(String name, Integer departmentId, String position) {
        if (name != null && !name.isEmpty()) {
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


    @Transactional
    public String getDoctorsOccupancyStats(LocalDate startDate, LocalDate endDate) {
        List<Doctor> doctors = (List<Doctor>) doctorsRepository.findAll();
        List<Department> departments = (List<Department>) departmentsRepository.findAll();
        Map<Integer, String> departmentIdToNameMap = departments.stream()
                .collect(Collectors.toMap(Department::id, Department::name));
        List<LocalDate> dates = startDate.datesUntil(endDate.plusDays(1)).toList();

        StringWriter writer = new StringWriter();

        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            // Header
            csvPrinter.print("Department");
            csvPrinter.print("Doctor");
            for (LocalDate date : dates) {
                csvPrinter.print(date.toString());
            }
            csvPrinter.println();

            // Data rows
            for (Doctor doctor : doctors) {
                String departmentName = departmentIdToNameMap.get(doctor.departmentId());
                csvPrinter.print(departmentName);
                csvPrinter.print(doctor.name());
                for (LocalDate date : dates) {
                    Long numberOfTreatments = treatmentsRepository.countTreatmentsByDateAndDoctor(date, doctor.id());
                    numberOfTreatments = (numberOfTreatments == null) ? 0 : numberOfTreatments;
                    csvPrinter.print(numberOfTreatments);
                }
                csvPrinter.println();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return writer.toString();
    }
}

