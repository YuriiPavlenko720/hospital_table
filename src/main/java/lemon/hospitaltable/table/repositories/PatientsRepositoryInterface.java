package lemon.hospitaltable.table.repositories;

import lemon.hospitaltable.table.objects.Patient;
import org.springframework.data.repository.CrudRepository;
import java.time.LocalDate;
import java.util.List;

public interface PatientsRepositoryInterface extends CrudRepository<Patient, Long> {

    List<Patient> findByName(String name);

    List<Patient> findByStatus(String status);

    List<Patient> findByNameAndBirth (String name, LocalDate birth);
}
