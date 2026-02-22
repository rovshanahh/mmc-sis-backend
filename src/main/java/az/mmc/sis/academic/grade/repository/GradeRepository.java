package az.mmc.sis.academic.grade.repository;

import az.mmc.sis.academic.grade.model.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    Optional<Grade> findByStudentIdAndTeachingAssignmentId(Long studentId, Long teachingAssignmentId);

    List<Grade> findByStudentId(Long studentId);

    List<Grade> findAllByTeachingAssignmentIdIn(Collection<Long> assignmentIds);
}
