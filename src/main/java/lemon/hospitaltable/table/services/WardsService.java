package lemon.hospitaltable.table.services;

import lemon.hospitaltable.table.objects.Department;
import lemon.hospitaltable.table.objects.Treatment;
import lemon.hospitaltable.table.objects.Ward;
import lemon.hospitaltable.table.objects.WardRequest;
import lemon.hospitaltable.table.repositories.DepartmentsRepositoryInterface;
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
import java.util.stream.StreamSupport;

@AllArgsConstructor
@Service
public class WardsService {

    private final WardsRepositoryInterface wardsRepository;
    private final TreatmentsRepositoryInterface treatmentsRepository;
    private final DepartmentsRepositoryInterface departmentsRepository;

    public Ward save(WardRequest wardRequest) {

        //checking department existence
        Department department = departmentsRepository.findById(wardRequest.departmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department ID " + wardRequest.departmentId() + " not found."));

        //checking capacity
        if (wardRequest.capacity() < 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }

        //creating of the ward
        return wardsRepository.save(new Ward(
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

        //checking new department existence
        Department department = departmentsRepository.findById(newDepartmentId)
                .orElseThrow(() -> new IllegalArgumentException("Department ID " + newDepartmentId + " not found."));

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
        //checking new capacity
        if (newCapacity < 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }

        //checking existence and getting the aim ward
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


    public List<Ward> findWards(Integer level, String name, Integer departmentId) {
        if (level != null) {
            return wardsRepository.findByLevel(level);
        } else if (name != null && !name.isEmpty()) {
            return wardsRepository.findByName(name);
        } else if (departmentId != null) {
            return wardsRepository.findByDepartmentId(departmentId);
        } else {
            return StreamSupport.stream(wardsRepository.findAll().spliterator(), false)
                    .collect(Collectors.toList());
        }
    }


    @Transactional
    public Map<String, DepartmentsOccupancyStats> getWardsOccupancyStats(LocalDate date) {
        List<Ward> wards = (List<Ward>) wardsRepository.findAll();
        List<Department> departments = (List<Department>) departmentsRepository.findAll();

        Map<Integer, String> departmentIdToNameMap = departments.stream()
                .collect(Collectors.toMap(Department::id, Department::name));

        Map<String, List<Ward>> wardsByDepartment = wards.stream()
                .collect(Collectors.groupingBy(ward -> departmentIdToNameMap.get(ward.departmentId())));

        return wardsByDepartment.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> {
                    List<Ward> departmentWards = entry.getValue();
                    int totalCapacity = departmentWards.stream().mapToInt(Ward::capacity).sum();
                    int totalTaken = departmentWards.stream()
                            .mapToInt(ward -> {
                                Integer count = treatmentsRepository.countTreatmentsInWardOnDate(ward.id(), date);
                                return count != null ? count : 0;
                            }).sum();
                    double occupancyRate = ((double) totalTaken / totalCapacity) * 100;
                    Map<String, Integer> wardFree = departmentWards.stream()
                            .collect(Collectors.toMap(Ward::name, ward -> {
                                Integer treatments = treatmentsRepository.countTreatmentsInWardOnDate(ward.id(), date);
                                treatments = (treatments == null) ? 0 : treatments;
                                return ward.capacity() - treatments;
                            }));
                    return new DepartmentsOccupancyStats(occupancyRate, wardFree);
                }));
    }

    public record DepartmentsOccupancyStats(double occupancyRate, Map<String, Integer> freeByWard) {
    }
}
