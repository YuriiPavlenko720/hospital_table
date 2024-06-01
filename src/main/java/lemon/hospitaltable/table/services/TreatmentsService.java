package lemon.hospitaltable.table.services;

import lemon.hospitaltable.table.objects.Treatment;
import lemon.hospitaltable.table.repositories.TreatmentsRepositoryInterface;
import lemon.hospitaltable.table.repositories.WardsRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class TreatmentsService {
    private final TreatmentsRepositoryInterface treatmentsRepository;
    private final WardsRepositoryInterface wardsRepository;

    @Autowired
    public TreatmentsService(TreatmentsRepositoryInterface treatmentsRepository, WardsRepositoryInterface wardsRepository) {
        this.treatmentsRepository = treatmentsRepository;
        this.wardsRepository = wardsRepository;
    }

    public void save(Long patientId, Integer doctorId, Integer wardId, Date dateIn, Date dateOut, String notation) {

        //getting capacity of ward
        Integer capacity = wardsRepository.getCapacityById(wardId);
        if (capacity == null) {
            throw new IllegalArgumentException("Room not found");
        }

        //checking taken less than capacity on each day of planned treatment
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateIn);
        while (!calendar.getTime().after(dateOut)) {
            Integer taken = treatmentsRepository.countTreatmentsOnDate(wardId, new Date(calendar.getTimeInMillis()));
            taken = (taken == null) ? 0 : taken;

            if (taken >= capacity) {
                throw new IllegalArgumentException("Not enough available place in the ward for the specified dates.");
            }

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        //creating treatment
        Treatment treatment = new Treatment(null, patientId, doctorId, wardId, dateIn, dateOut, notation);
        treatmentsRepository.save(treatment);
    }

    public void deleteById(Long id) {
        treatmentsRepository.deleteById(id);
    }

    public void changePatientIdById(Long id, Long patientId) {
        treatmentsRepository.changePatientIdById(id, patientId);
    }

    public void changeDoctorIdById(Long id, Integer doctorId) {
        treatmentsRepository.changeDoctorIdById(id, doctorId);
    }

    public void changeWardIdById(Long id, Integer wardId) {
        treatmentsRepository.changeWardIdById(id, wardId);
    }

    public void changeDateInById(Long id, Date dateIn) {
        treatmentsRepository.changeDateInById(id, dateIn);
    }

    public void changeDateOutById(Long id, Date dateOut) {
        treatmentsRepository.changeDateOutById(id, dateOut);
    }

    public void changeNotationById(Long id, String notation) {
        treatmentsRepository.changeNotationById(id, notation);
    }

    public Optional<Treatment> findById(Long id) {
        return treatmentsRepository.findById(id);
    }

    public List<Treatment> findAll() {
        return (List<Treatment>) treatmentsRepository.findAll();
    }

    public List<Treatment> findByPatientId(Long patientId) {
        return treatmentsRepository.findByPatientId(patientId);
    }

    public List<Treatment> findByDoctorId(Integer doctorId) {
        return treatmentsRepository.findByDoctorId(doctorId);
    }

    public List<Treatment> findByWardId(Integer wardId) {
        return treatmentsRepository.findByWardId(wardId);
    }
}
