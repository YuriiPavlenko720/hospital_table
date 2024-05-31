package lemon.hospitaltable.table.services;

import lemon.hospitaltable.table.objects.Ward;
import lemon.hospitaltable.table.repositories.WardsRepositoryInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class WardsService {
    private final WardsRepositoryInterface wardsRepository;

    @Autowired
    public WardsService(WardsRepositoryInterface wardsRepository) {
        this.wardsRepository = wardsRepository;
    }

    public void save(Integer level, String name, Integer departmentId,
                     Integer capacity) {
        Ward ward = new Ward(null, level, name, departmentId, capacity, 0, 0);
        wardsRepository.save(ward);
    }

    public void deleteById(Integer id) {
        wardsRepository.deleteById(id);
    }

    public void changeLevelById(Integer id, Integer level) {
        wardsRepository.changeLevelById(id, level);
    }

    public void renameById(Integer id, String name) {
        wardsRepository.renameById(id, name);
    }

    public void changeDepartmentById(Integer id, Integer departmentId) {
        wardsRepository.changeDepartmentById(id, departmentId);
    }

    public void changeCapacityById(Integer id, Integer capacity) {
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
}
