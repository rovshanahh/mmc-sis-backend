package az.mmc.sis.academic.attendance.repository;

import az.mmc.sis.academic.attendance.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    boolean existsByLessonIdAndStudentId(Long lessonId, Long studentId);

    List<Attendance> findByStudentId(Long studentId);

    List<Attendance> findAllByLessonIdIn(Collection<Long> lessonIds);
}
