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

        //checking order of dates
        if (dateOut.before(dateIn)) {
            throw new IllegalArgumentException("End treating date cannot be before start treating date.");
        }

        //checking overtreatment
        Integer overtreatment = treatmentsRepository.countOvertreatmentsByPatientId(patientId, dateIn, dateOut, null);
        if (overtreatment > 0) {
            throw new IllegalArgumentException("This patient have already a treatment at these dates.");
        }

        //getting capacity of ward
        Integer capacity = wardsRepository.getCapacityById(wardId);
        if (capacity == null) {
            throw new IllegalArgumentException("Ward not found.");
        }

        //checking taken less than capacity on each day of planned treatment
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateIn);
        while (!calendar.getTime().after(dateOut)) {
            Integer taken = treatmentsRepository.countTreatmentsInWardOnDate(wardId, new Date(calendar.getTimeInMillis()));
            taken = (taken == null) ? 0 : taken;

            if (taken >= capacity) {
                throw new IllegalArgumentException("Not enough available places in the ward for the specified dates.");
            }

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        //creating treatment
        Treatment treatment = new Treatment(null, patientId, doctorId, wardId, dateIn, dateOut, notation);
        treatmentsRepository.save(treatment);
        //DOCTOR NOTICE?
    }

    public void deleteById(Long id) {
        treatmentsRepository.deleteById(id);
        //DOCTOR NOTICE?
    }

    public void changePatientIdById(Long id, Long patientId) {
        treatmentsRepository.changePatientIdById(id, patientId);
        //DOCTOR NOTICE?
    }

    public void changeDoctorIdById(Long id, Integer doctorId) {
        treatmentsRepository.changeDoctorIdById(id, doctorId);
        //DOCTOR NOTICE?
        //DOCTOR NOTICE?
    }

    public void changeWardIdById(Long id, Integer wardId) {
        Treatment treatment = treatmentsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Treatment not found."));

        //getting capacity of new ward
        Integer capacity = wardsRepository.getCapacityById(wardId);
        if (capacity == null) {
            throw new IllegalArgumentException("Ward not found.");
        }

        //checking taken less than capacity on each day of planned treatment in new ward
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(treatment.dateIn());
        while (!calendar.getTime().after(treatment.dateOut())) {
            checkingCapacity(treatment, capacity, calendar);
        }

        //setting new ward
        treatmentsRepository.changeWardIdById(id, wardId);
        //DOCTOR NOTICE?
    }

    public void changeDateInById(Long id, Date newDateIn) {
        //getting the aim treatment
        Treatment treatment = treatmentsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Treatment not found."));

        //checking order of dates
        if (treatment.dateOut().before(newDateIn)) {
            throw new IllegalArgumentException("End treating date cannot be before start treating date.");
        }

        //checking overtreatment
        Integer overtreatment = treatmentsRepository.countOvertreatmentsByPatientId(
                treatment.patientId(), newDateIn, treatment.dateOut(), id);
        if (overtreatment > 0) {
            throw new IllegalArgumentException("This patient have already a treatment at these dates.");
        }

        //getting capacity of ward
        Integer capacity = wardsRepository.getCapacityById(treatment.wardId());
        if (capacity == null) {
            throw new IllegalArgumentException("Ward not found.");
        }

        //checking taken less than capacity on each day of planned extension
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(newDateIn);
        while (!calendar.getTime().after(treatment.dateIn())) {
            checkingCapacity(treatment, capacity, calendar);
        }

        //setting new dateIn
        treatmentsRepository.changeDateInById(id, newDateIn);
        //DOCTOR NOTICE?
    }

    public void changeDateOutById(Long id, Date newDateOut) {
        //getting the aim treatment
        Treatment treatment = treatmentsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Treatment not found."));

        //checking order of dates
        if (newDateOut.before(treatment.dateIn())) {
            throw new IllegalArgumentException("End treating date cannot be before start treating date.");
        }

        //checking overtreatment
        Integer overtreatment = treatmentsRepository.countOvertreatmentsByPatientId(
                treatment.patientId(), treatment.dateIn(), newDateOut, id);
        if (overtreatment > 0) {
            throw new IllegalArgumentException("This patient have already a treatment at these dates.");
        }

        //getting capacity of ward
        Integer capacity = wardsRepository.getCapacityById(treatment.wardId());
        if (capacity == null) {
            throw new IllegalArgumentException("Ward not found.");
        }

        //checking taken less than capacity on each day of planned extension
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(treatment.dateOut());
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        while (!calendar.getTime().after(newDateOut)) {
            checkingCapacity(treatment, capacity, calendar);
        }

        //setting new dateOut
        treatmentsRepository.changeDateOutById(id, newDateOut);
        //DOCTOR NOTICE?
    }

    private void checkingCapacity(Treatment treatment, Integer capacity, Calendar calendar) {
        Integer taken = treatmentsRepository.countTreatmentsInWardOnDate(treatment.wardId(), new Date(calendar.getTimeInMillis()));
        taken = (taken == null) ? 0 : taken;

        if (taken >= capacity) {
            throw new IllegalArgumentException("Not enough available places in the ward for the new dates.");
        }

        calendar.add(Calendar.DAY_OF_MONTH, 1);
    }

    public void changeNotationById(Long id, String notation) {
        treatmentsRepository.changeNotationById(id, notation);
        //DOCTOR NOTICE?
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
