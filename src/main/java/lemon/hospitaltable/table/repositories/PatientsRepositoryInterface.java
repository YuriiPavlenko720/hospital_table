package lemon.hospitaltable.table.repositories;

import lemon.hospitaltable.table.objects.Patient;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import java.sql.Date;
import java.util.List;

public interface PatientsRepositoryInterface extends CrudRepository<Patient, Long> {
    @Modifying
    @Query("UPDATE patients SET name = :name WHERE id = :id")
    void renameById(Long id, String name);

    @Modifying
    @Query("UPDATE patients SET birth = :birth WHERE id = :id")
    void changeBirthById(Long id, Date birth);

    @Modifying
    @Query("UPDATE patients SET address = :address WHERE id = :id")
    void changeAddressById(Long id, String address);

    @Modifying
    @Query("UPDATE patients SET status = :status WHERE id = :id")
    void changeStatusById(Long id, String status);

    @Modifying
    @Query("UPDATE patients SET notation = :notation WHERE id = :id")
    void changeNotationById(Long id, String notation);

    List<Patient> findByName(String name);

    List<Patient> findByStatus(String status);
}
