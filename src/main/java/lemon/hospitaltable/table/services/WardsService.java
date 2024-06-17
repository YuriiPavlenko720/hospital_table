package lemon.hospitaltable.table.services;

import lemon.hospitaltable.table.controllers.WardsController;
import lemon.hospitaltable.table.objects.Treatment;
import lemon.hospitaltable.table.objects.Ward;
import lemon.hospitaltable.table.repositories.TreatmentsRepositoryInterface;
import lemon.hospitaltable.table.repositories.WardsRepositoryInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class WardsService {

    private final WardsRepositoryInterface wardsRepository;
    private final TreatmentsRepositoryInterface treatmentsRepository;

    public void save(WardsController.WardRequest wardRequest) {
        wardsRepository.save(new Ward(
                null,
                wardRequest.level(),
                wardRequest.name(),
                wardRequest.departmentId(),
                wardRequest.capacity()
        ));
    }


    @Transactional
    public void deleteById(Integer id) {
        //checking existing of the aim ward
        Ward ward = wardsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ward ID " + id + " not found."));

        //checking treatments existing in aim ward
        List<Treatment> treatments = treatmentsRepository.findByWardId(id);
        for (Treatment treatment : treatments) {
            if (treatment.dateOut().isAfter(LocalDate.now())) {
                throw new IllegalArgumentException(
                        "Cannot delete ward ID " + id + " with active or planned treatments in the ward: \n" +
                                treatment
                );
            }
        }

        //deleting of the aim ward
        wardsRepository.deleteById(id);
    }


    public void changeLevelById(Integer id, Integer newLevel) {
        Ward ward = wardsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ward ID " + id + " not found."));
        wardsRepository.save(ward.withLevel(newLevel));
    }


    public void renameById(Integer id, String newName) {
        Ward ward = wardsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ward ID " + id + " not found."));
        wardsRepository.save(ward.withName(newName));
    }


    @Transactional
    public void changeDepartmentById(Integer id, Integer newDepartmentId) {
        //checking existence and getting the aim ward
        Ward ward = wardsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ward ID " + id + " not found."));

        //checking treatments existing in the aim ward
        List<Treatment> treatments = treatmentsRepository.findByWardId(id);
        for (Treatment treatment : treatments) {
            if (treatment.dateOut().isAfter(LocalDate.now())) {
                throw new IllegalArgumentException(
                        "Cannot change department of the ward ID " + id +
                                " with active or planned treatments in the ward: \n" +
                                treatment
                );
            }
        }

        //saving ward with the new department
        wardsRepository.save(ward.withDepartmentId(newDepartmentId));
    }


    @Transactional
    public void changeCapacityById(Integer id, Integer newCapacity) {
        //checking existence and getting of the aim ward
        Ward ward = wardsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ward ID " + id + " not found."));

        //checking taken less than capacity on each day in ward with new capacity
        List<Treatment> treatments = treatmentsRepository.findByWardId(id);
        for (Treatment treatment : treatments) {

            for (
                    LocalDate date = treatment.dateIn();
                    !date.isAfter(treatment.dateOut());
                    date = date.plusDays(1)
            ) {
                int treatmentsCount = treatmentsRepository.countTreatmentsInWardOnDate(id, date);
                if (treatmentsCount > newCapacity) {
                    throw new IllegalArgumentException("New capacity of ward ID " + id +
                            " is less than current taken on date: " + date);
                }
            }
        }

        //saving ward with the new capacity
        wardsRepository.save(ward.withCapacity(newCapacity));
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
    public Map<Integer, DepartmentsOccupancyStats> getWardsOccupancyStats(LocalDate date) {
        List<Ward> wards = (List<Ward>) wardsRepository.findAll();

        Map<Integer, Map<Integer, Integer>> freeBedsByDepartmentAndWard = wards.stream()
                .collect(Collectors.groupingBy(Ward::departmentId,
                        Collectors.toMap(Ward::id, ward -> {
                            Integer taken = treatmentsRepository.countTreatmentsInWardOnDate(ward.id(), date);
                            taken = (taken == null) ? 0 : taken;
                            return ward.capacity() - taken;
                        })));

        return freeBedsByDepartmentAndWard.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                    int departmentCapacity = wards.stream().filter(ward -> ward.departmentId().equals(entry.getKey())).mapToInt(Ward::capacity).sum();
                    int departmentTaken = departmentCapacity - entry.getValue().values().stream().mapToInt(Integer::intValue).sum();
                    double occupancyRate = ((double) departmentTaken / departmentCapacity) * 100;
                    return new DepartmentsOccupancyStats(entry.getValue(), occupancyRate);
                }));
    }

    public record DepartmentsOccupancyStats(Map<Integer, Integer> freeByWard, double occupancyRate) {
    }
}
