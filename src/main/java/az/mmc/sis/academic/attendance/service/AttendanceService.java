package az.mmc.sis.academic.attendance.service;

import az.mmc.sis.academic.attendance.dto.AttendanceCreateRequest;
import az.mmc.sis.academic.attendance.dto.AttendanceResponse;
import az.mmc.sis.academic.attendance.dto.StudentAttendanceResponse;
import az.mmc.sis.academic.attendance.dto.StudentAttendanceSummaryResponse;
import az.mmc.sis.academic.attendance.mapper.AttendanceMapper;
import az.mmc.sis.academic.attendance.model.Attendance;
import az.mmc.sis.academic.attendance.repository.AttendanceRepository;
import az.mmc.sis.academic.lesson.model.Lesson;
import az.mmc.sis.academic.lesson.repository.LessonRepository;
import az.mmc.sis.common.exception.ConflictException;
import az.mmc.sis.common.exception.ForbiddenException;
import az.mmc.sis.common.exception.NotFoundException;
import az.mmc.sis.user.model.Role;
import az.mmc.sis.user.model.User;
import az.mmc.sis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;

    @Transactional
    public AttendanceResponse recordAttendance(AttendanceCreateRequest request, String teacherEmail) {

        User teacher = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new NotFoundException("Teacher not found"));

        Lesson lesson = lessonRepository.findById(request.lessonId())
                .orElseThrow(() -> new NotFoundException("Lesson not found"));

        if (!lesson.getTeachingAssignment().getTeacher().getId().equals(teacher.getId())) {
            throw new ForbiddenException("You can only record attendance for your own lessons");
        }

        User student = userRepository.findById(request.studentId())
                .orElseThrow(() -> new NotFoundException("Student not found"));

        if (student.getRole() != Role.STUDENT) {
            throw new ForbiddenException("User is not a student");
        }

        if (student.getGroup() == null || student.getGroup().getId() == null) {
            throw new ForbiddenException("Student does not belong to any group");
        }

        if (!student.getGroup().getId().equals(lesson.getTeachingAssignment().getGroup().getId())) {
            throw new ForbiddenException("Student does not belong to this group");
        }

        // Professional: duplicates -> 409 (unique constraint is lesson_id + student_id)
        if (attendanceRepository.existsByLessonIdAndStudentId(lesson.getId(), student.getId())) {
            throw new ConflictException("Attendance already recorded for this student in this lesson");
        }

        Attendance saved = attendanceRepository.save(
                Attendance.builder()
                        .lesson(lesson)
                        .student(student)
                        .status(request.status())
                        .build()
        );

        return AttendanceMapper.toTeacherResponse(saved);
    }

    public StudentAttendanceSummaryResponse getMyAttendance(String studentEmail) {

        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new NotFoundException("Student not found"));

        List<Attendance> attendances = attendanceRepository.findByStudentId(student.getId());

        List<StudentAttendanceResponse> records = attendances.stream()
                .map(AttendanceMapper::toStudentRecord)
                .toList();

        int totalLessons = records.size();
        int presentCount = (int) records.stream().filter(r -> "PRESENT".equals(r.status())).count();

        double percentage = totalLessons == 0 ? 0 : ((double) presentCount / totalLessons) * 100;

        return StudentAttendanceSummaryResponse.builder()
                .records(records)
                .totalLessons(totalLessons)
                .presentCount(presentCount)
                .participationPercentage(percentage)
                .build();
    }
}
