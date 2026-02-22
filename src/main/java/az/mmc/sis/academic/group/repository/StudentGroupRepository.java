package az.mmc.sis.academic.group.repository;

import az.mmc.sis.academic.group.model.StudentGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentGroupRepository extends JpaRepository<StudentGroup, Long> {

    boolean existsByDepartmentIdAndNameIgnoreCase(Long departmentId, String name);

}
