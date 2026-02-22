package az.mmc.sis.academic.assignment.repository;

import az.mmc.sis.academic.assignment.model.TeachingAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeachingAssignmentRepository extends JpaRepository<TeachingAssignment, Long> {

    Optional<TeachingAssignment> findByTeacherIdAndSubjectIdAndGroupId(Long teacherId, Long subjectId, Long groupId);

    List<TeachingAssignment> findAllByTeacherId(Long teacherId);

    List<TeachingAssignment> findAllBySubjectId(Long subjectId);

    List<TeachingAssignment> findAllByGroupId(Long groupId);

    List<TeachingAssignment> findByTeacherId(Long teacherId);
}
