package lemon.hospitaltable.table.repositories;

import lemon.hospitaltable.table.objects.Treatment;
import lemon.hospitaltable.table.objects.TreatmentStats;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface TreatmentsRepositoryInterface extends CrudRepository<Treatment, Long> {

    @Query("""
            SELECT
                *
            FROM
                treatments
            WHERE
                    patient_id = :patientId
                AND
                    ((date_in <= :dateOut AND date_out >= :dateIn) AND id != :id)
            """)
    List<Treatment> findOvertreatmentsByPatientId(
            @Param("patientId") Long patientId,
            @Param("dateIn") LocalDate dateIn,
            @Param("dateOut") LocalDate dateOut,
            @Param("id") Long id
    );


    @Query("""
            SELECT
                COUNT(*)
            FROM
                treatments
            WHERE
                    ward_id = :wardId
                AND
                    (date_in <= :date AND date_out >= :date)
            """)
    Integer countTreatmentsInWardOnDate(
            @Param("wardId") Integer roomId,
            @Param("date") LocalDate date
    );


    @Query("""
            SELECT
                COUNT(*)
            FROM
                treatments
            WHERE
                    doctor_id = :doctorId
                AND
                    (date_in <= :date AND date_out >= :date)
            """)
    Long countTreatmentsByDateAndDoctor(LocalDate date, Integer doctorId);


    @Query("""
            SELECT
                wards.department_id AS departmentId,
                COUNT(*) AS count
            FROM
                    treatments
                JOIN
                    wards
                ON treatments.ward_id = wards.id
            WHERE
                treatments.date_out BETWEEN :startDate AND :endDate
            GROUP BY wards.department_id
            """)
    List<TreatmentStats> findAllTreatmentsEndingBetweenByDepartments(
            LocalDate startDate,
            LocalDate endDate
    );


    @Query("""
            SELECT
                COUNT(*)
            FROM
                treatments
            WHERE
                date_out BETWEEN :startDate AND :endDate
            """)
    Integer countTreatmentsEndingBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


    @Query("""
            SELECT
                COUNT(*)
            FROM
                treatments
            WHERE
                doctor_id = :doctorId AND date_out BETWEEN :startDate AND :endDate
            """)
    Integer countTreatmentsEndingBetweenByDoctorId(
            @Param("doctorId") Integer doctorId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


    @Query("""
            SELECT
                COUNT(treatments.id) AS count
            FROM
                    treatments
                JOIN
                    wards
                ON treatments.ward_id = wards.id
            WHERE
                    wards.department_id = :departmentId
                AND
                    treatments.date_out BETWEEN :startDate AND :endDate
            """)
    Integer countTreatmentsEndingBetweenByDepartmentId(
            @Param("departmentId") Integer departmentId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );


    List<Treatment> findByPatientId(Long patientId);


    List<Treatment> findByDoctorId(Integer doctorId);


    List<Treatment> findByWardId(Integer wardId);
}
