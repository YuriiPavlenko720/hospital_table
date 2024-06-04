package lemon.hospitaltable.table.services;

import lemon.hospitaltable.table.objects.DepartmentsOccupancyStats;
import lemon.hospitaltable.table.objects.Treatment;
import lemon.hospitaltable.table.objects.Ward;
import lemon.hospitaltable.table.repositories.TreatmentsRepositoryInterface;
import lemon.hospitaltable.table.repositories.WardsRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WardsService {
    private final WardsRepositoryInterface wardsRepository;
    private final TreatmentsRepositoryInterface treatmentsRepository;

    @Autowired
    public WardsService(WardsRepositoryInterface wardsRepository, TreatmentsRepositoryInterface treatmentsRepository) {
        this.wardsRepository = wardsRepository;
        this.treatmentsRepository = treatmentsRepository;
    }

    public void save(Integer level, String name, Integer departmentId,
                     Integer capacity) {
        Ward ward = new Ward(null, level, name, departmentId, capacity, 0, 0);
        wardsRepository.save(ward);
    }

    @Transactional
    public void deleteById(Integer id) {
        //checking existing of the aim ward
        Ward ward = wardsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ward not found."));

        //checking treatments existing in aim ward
        List<Treatment> treatments = treatmentsRepository.findByWardId(id);
        for (Treatment treatment : treatments) {
            if (treatment.dateOut().after(new Date(System.currentTimeMillis()))) {
                throw new IllegalArgumentException("Cannot delete ward with active or planned treatments in the ward.");
            }
        }

        //deleting of the aim ward
        wardsRepository.deleteById(id);
    }

    public void changeLevelById(Integer id, Integer level) {
        wardsRepository.changeLevelById(id, level);
    }

    public void renameById(Integer id, String name) {
        wardsRepository.renameById(id, name);
    }

    @Transactional
    public void changeDepartmentById(Integer id, Integer departmentId) {
        //checking existing of the aim ward
        Ward ward = wardsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ward not found."));

        //checking treatments existing in aim ward
        List<Treatment> treatments = treatmentsRepository.findByWardId(id);
        for (Treatment treatment : treatments) {
            if (treatment.dateOut().after(new Date(System.currentTimeMillis()))) {
                throw new IllegalArgumentException("Cannot change department with active or planned treatments in the ward.");
            }
        }

        //changing department of the aim ward
        wardsRepository.changeDepartmentById(id, departmentId);
    }

    @Transactional
    public void changeCapacityById(Integer id, Integer capacity) {
        //checking existing of the aim ward
        Ward ward = wardsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ward not found."));

        //checking taken less than capacity on each day in ward with new capacity
        List<Treatment> treatments = treatmentsRepository.findByWardId(id);
        for (Treatment treatment : treatments) {
            if (treatment.dateOut().after(new Date(System.currentTimeMillis()))) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(treatment.dateIn());
                while (!calendar.getTime().after(treatment.dateOut())) {
                    Integer taken = treatmentsRepository.countTreatmentsInWardOnDate(id, new Date(calendar.getTimeInMillis()));
                    taken = (taken == null) ? 0 : taken;
                    if (taken > capacity) {
                        throw new IllegalArgumentException("New capacity is less than the number of places already booked.");
                    }
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                }
            }
        }

        //setting the new capacity
        wardsRepository.changeCapacityById(id, capacity);
    }

    public void changeTakenById(Integer id, Integer taken) {
        wardsRepository.changeTakenById(id, taken);
    }

    public void changeFreeById(Integer id, Integer free) {
        wardsRepository.changeFreeById(id, free);
    }

    public Optional<Ward> findById(Integer id) {
        return wardsRepository.findById(id);
    }

    public List<Ward> findAll() {
        return (List<Ward>) wardsRepository.findAll();
    }

    public List<Ward> findByLevel(Integer level) {
        return wardsRepository.findByLevel(level);
    }

    public List<Ward> findByName(String name) {
        return wardsRepository.findByName(name);
    }

    public List<Ward> findByDepartmentId(Integer departmentId) {
        return wardsRepository.findByDepartmentId(departmentId);
    }

    @Transactional
    public Map<Integer, DepartmentsOccupancyStats> getWardsOccupancyStats(Date date) {
        List<Ward> wards = (List<Ward>) wardsRepository.findAll();

        Map<Integer, Map<Integer, Integer>> freeBedsByDepartmentAndWard = wards.stream()
                .collect(Collectors.groupingBy(Ward::departmentId,
                        Collectors.toMap(Ward::id, ward -> {
                            Integer taken = treatmentsRepository.countTreatmentsInWardOnDate(ward.id(), date);
                            taken = (taken == null) ? 0 : taken;
                            return ward.capacity() - taken;
                        })));

        Map<Integer, DepartmentsOccupancyStats> departmentsOccupancyStats = freeBedsByDepartmentAndWard.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                    int departmentCapacity = wards.stream().filter(ward -> ward.departmentId().equals(entry.getKey())).mapToInt(Ward::capacity).sum();
                    int departmentTaken = departmentCapacity - entry.getValue().values().stream().mapToInt(Integer::intValue).sum();
                    double occupancyRate = ((double) departmentTaken / departmentCapacity) * 100;
                    return new DepartmentsOccupancyStats(entry.getValue(), occupancyRate);
                }));

        return departmentsOccupancyStats;
    }
}
