package az.mmc.sis.academic.department.repository;

import az.mmc.sis.academic.department.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    boolean existsByFacultyIdAndNameIgnoreCase(Long facultyId, String name);

    boolean existsByFacultyIdAndNameIgnoreCaseAndIdNot(Long facultyId, String name, Long id);

    List<Department> findAllByFacultyIdOrderByNameAsc(Long facultyId);
}
