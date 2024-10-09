package lemon.hospitaltable.table.repositories;

import lemon.hospitaltable.table.objects.Love;
import lemon.hospitaltable.table.objects.loveResponses.DoctorRatingResponse;
import lemon.hospitaltable.table.objects.loveResponses.PatientRatingResponse;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface LovesRepositoryInterface extends CrudRepository<Love, Long> {

    Optional<Love> findByPatientIdAndDoctorId(Long patientId, Integer doctorId);

    List<Love> findByPatientId(Long patientId);

    List<Love> findByDoctorId(Integer doctorId);

    void deleteByPatientId(Long patientId);

    @Query("""
              SELECT
                  doctor_id,
                  AVG(rating) as average_rating
              FROM
                  loves
              GROUP BY doctor_id
              ORDER BY average_rating DESC
              LIMIT 3
              """)
    List<DoctorRatingResponse> findTopDoctors();

    @Query("""
              SELECT
                  doctor_id,
                  AVG(rating) as average_rating
              FROM
                  loves
              GROUP BY doctor_id
              ORDER BY average_rating
              LIMIT 3
              """)
    List<DoctorRatingResponse> findBottomDoctors();

    @Query("""
            SELECT
                patient_id,
                COUNT(*) as rating_count
            FROM
                loves
            GROUP BY patient_id
            ORDER BY rating_count DESC
            LIMIT 5
            """)
    List<PatientRatingResponse> findTopPatients();
}
