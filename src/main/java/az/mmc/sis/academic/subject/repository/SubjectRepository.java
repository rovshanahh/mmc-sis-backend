package az.mmc.sis.academic.subject.repository;

import az.mmc.sis.academic.subject.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    boolean existsByDepartmentIdAndNameIgnoreCase(Long departmentId, String name);

    boolean existsByDepartmentIdAndNameIgnoreCaseAndIdNot(Long departmentId, String name, Long id);
}
