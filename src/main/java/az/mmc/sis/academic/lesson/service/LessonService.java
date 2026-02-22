package az.mmc.sis.academic.lesson.service;

import az.mmc.sis.academic.assignment.model.TeachingAssignment;
import az.mmc.sis.academic.assignment.repository.TeachingAssignmentRepository;
import az.mmc.sis.academic.attendance.dto.AttendanceRequest;
import az.mmc.sis.academic.attendance.dto.LessonStudentResponse;
import az.mmc.sis.academic.attendance.model.Attendance;
import az.mmc.sis.academic.attendance.repository.AttendanceRepository;
import az.mmc.sis.academic.lesson.dto.LessonRequest;
import az.mmc.sis.academic.lesson.dto.LessonResponse;
import az.mmc.sis.academic.lesson.mapper.LessonMapper;
import az.mmc.sis.academic.lesson.model.Lesson;
import az.mmc.sis.academic.lesson.repository.LessonRepository;
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
public class LessonService {

    private final LessonRepository lessonRepository;
    private final TeachingAssignmentRepository assignmentRepository;
    private final UserRepository userRepository;
    private final AttendanceRepository attendanceRepository;

    @Transactional
    public LessonResponse createLesson(LessonRequest request, String teacherEmail) {

        User teacher = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new NotFoundException("Teacher not found"));

        TeachingAssignment assignment = assignmentRepository.findById(request.assignmentId())
                .orElseThrow(() -> new NotFoundException("Assignment not found"));

        if (!assignment.getTeacher().getId().equals(teacher.getId())) {
            throw new ForbiddenException("You can only create lessons for your own assignments");
        }

        Lesson saved = lessonRepository.save(
                Lesson.builder()
                        .date(request.date())
                        .teachingAssignment(assignment)
                        .build()
        );

        return LessonMapper.toResponse(saved);
    }

    public List<LessonResponse> getMyLessons(String teacherEmail) {

        User teacher = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new NotFoundException("Teacher not found"));

        return lessonRepository.findByTeachingAssignmentTeacherId(teacher.getId())
                .stream()
                .map(LessonMapper::toResponse)
                .toList();
    }

    public List<LessonStudentResponse> getStudentsForLesson(Long lessonId, String teacherEmail) {

        User teacher = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new NotFoundException("Teacher not found"));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Lesson not found"));

        if (!lesson.getTeachingAssignment().getTeacher().getId().equals(teacher.getId())) {
            throw new ForbiddenException("You are not allowed to access this lesson");
        }

        Long groupId = lesson.getTeachingAssignment().getGroup().getId();

        return userRepository.findByGroupId(groupId)
                .stream()
                .filter(u -> u.getRole() == Role.STUDENT)
                .map(s -> LessonStudentResponse.builder()
                        .studentId(s.getId())
                        .studentName(s.getFirstName() + " " + s.getLastName())
                        .build())
                .toList();
    }

    @Transactional
    public void recordAttendance(Long lessonId, List<AttendanceRequest> requests, String teacherEmail) {

        User teacher = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new NotFoundException("Teacher not found"));

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NotFoundException("Lesson not found"));

        if (!lesson.getTeachingAssignment().getTeacher().getId().equals(teacher.getId())) {
            throw new ForbiddenException("Not allowed");
        }

        for (AttendanceRequest req : requests) {

            User student = userRepository.findById(req.studentId())
                    .orElseThrow(() -> new NotFoundException("Student not found"));

            boolean exists = attendanceRepository.existsByLessonIdAndStudentId(lessonId, student.getId());
            if (exists) continue;

            attendanceRepository.save(
                    Attendance.builder()
                            .lesson(lesson)
                            .student(student)
                            .status(req.status())
                            .build()
            );
        }
    }
}
