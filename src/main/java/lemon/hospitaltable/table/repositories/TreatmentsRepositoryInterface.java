package lemon.hospitaltable.table.repositories;

import lemon.hospitaltable.table.objects.Treatment;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import java.sql.Date;
import java.util.List;

public interface TreatmentsRepositoryInterface extends CrudRepository<Treatment, Long> {

    @Modifying
    @Query("UPDATE treatments SET patient_id = :patientId WHERE id = :id")
    void changePatientIdById(Long id, Long patientId);

    @Modifying
    @Query("UPDATE treatments SET doctor_id = :doctorId WHERE id = :id")
    void changeDoctorIdById(Long id, Integer doctorId);

    @Modifying
    @Query("UPDATE treatments SET ward_id = :wardId WHERE id = :id")
    void changeWardIdById(Long id, Integer wardId);

    @Modifying
    @Query("UPDATE treatments SET dateIn = :dateIn WHERE id = :id")
    void changeDateInById(Long id, Date dateIn);

    @Modifying
    @Query("UPDATE treatments SET dateOut = :dateOut WHERE id = :id")
    void changeDateOutById(Long id, Date dateOut);

    @Modifying
    @Query("UPDATE treatments SET notation = :notation WHERE id = :id")
    void changeNotationById(Long id, String notation);
}
