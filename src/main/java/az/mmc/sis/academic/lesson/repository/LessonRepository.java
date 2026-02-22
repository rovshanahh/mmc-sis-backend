package az.mmc.sis.academic.lesson.repository;

import az.mmc.sis.academic.lesson.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    // used by LessonService.getMyLessons(...)
    List<Lesson> findByTeachingAssignmentTeacherId(Long teacherId);

    // used by TeacherAnalyticsService
    List<Lesson> findAllByTeachingAssignmentIdIn(Collection<Long> teachingAssignmentIds);
}
