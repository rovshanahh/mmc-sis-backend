package az.mmc.sis.academic.analytics.service;

import az.mmc.sis.academic.analytics.dto.TeacherDashboardResponse;
import az.mmc.sis.academic.assignment.model.TeachingAssignment;
import az.mmc.sis.academic.assignment.repository.TeachingAssignmentRepository;
import az.mmc.sis.academic.attendance.model.Attendance;
import az.mmc.sis.academic.attendance.repository.AttendanceRepository;
import az.mmc.sis.academic.grade.model.Grade;
import az.mmc.sis.academic.grade.repository.GradeRepository;
import az.mmc.sis.academic.lesson.model.Lesson;
import az.mmc.sis.academic.lesson.repository.LessonRepository;
import az.mmc.sis.common.exception.NotFoundException;
import az.mmc.sis.user.model.Role;
import az.mmc.sis.user.model.User;
import az.mmc.sis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeacherAnalyticsService {

    private final UserRepository userRepository;
    private final TeachingAssignmentRepository teachingAssignmentRepository;
    private final LessonRepository lessonRepository;
    private final GradeRepository gradeRepository;
    private final AttendanceRepository attendanceRepository;

    public TeacherDashboardResponse getDashboard(String teacherEmail) {

        User teacher = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new NotFoundException("Teacher not found"));

        if (teacher.getRole() != Role.TEACHER) {
            throw new NotFoundException("Teacher not found");
        }

        List<TeachingAssignment> assignments = teachingAssignmentRepository.findAllByTeacherId(teacher.getId());
        Set<Long> assignmentIds = assignments.stream().map(TeachingAssignment::getId).collect(Collectors.toSet());

        List<Lesson> lessons = assignmentIds.isEmpty()
                ? List.of()
                : lessonRepository.findAllByTeachingAssignmentIdIn(assignmentIds);

        List<Grade> grades = assignmentIds.isEmpty()
                ? List.of()
                : gradeRepository.findAllByTeachingAssignmentIdIn(assignmentIds);

        List<Attendance> attendances = lessons.isEmpty()
                ? List.of()
                : attendanceRepository.findAllByLessonIdIn(lessons.stream().map(Lesson::getId).toList());

        long totalAssignments = assignments.size();
        long totalLessons = lessons.size();
        long totalGrades = grades.size();
        long totalAttendance = attendances.size();

        // -------- Grades by assignment --------
        Map<Long, List<Grade>> gradesByAssignment = grades.stream()
                .collect(Collectors.groupingBy(g -> g.getTeachingAssignment().getId()));

        List<TeacherDashboardResponse.GradeByAssignmentRow> gradeRows = assignments.stream()
                .map(a -> {
                    List<Grade> list = gradesByAssignment.getOrDefault(a.getId(), List.of());

                    double avgFinalScore = avg(list.stream().map(Grade::getFinalScore).toList());
                    double avgQuiz = avg(list.stream().map(Grade::getQuiz).toList());
                    double avgMid = avg(list.stream().map(Grade::getMidterm).toList());
                    double avgFin = avg(list.stream().map(Grade::getFinalExam).toList());

                    return TeacherDashboardResponse.GradeByAssignmentRow.builder()
                            .assignmentId(a.getId())
                            .subjectName(a.getSubject().getName())
                            .groupName(a.getGroup().getName())
                            .credit(a.getSubject().getCredit())
                            .gradedCount(list.size())
                            .avgFinalScore(round2(avgFinalScore))
                            .avgQuiz(round2(avgQuiz))
                            .avgMidterm(round2(avgMid))
                            .avgFinalExam(round2(avgFin))
                            .aCount(countLetter(list, "A"))
                            .bCount(countLetter(list, "B"))
                            .cCount(countLetter(list, "C"))
                            .dCount(countLetter(list, "D"))
                            .fCount(countLetter(list, "F"))
                            .build();
                })
                .sorted(Comparator.comparing(TeacherDashboardResponse.GradeByAssignmentRow::assignmentId))
                .toList();

        // -------- Attendance by lesson --------
        Map<Long, List<Attendance>> attByLesson = attendances.stream()
                .collect(Collectors.groupingBy(a -> a.getLesson().getId()));

        List<TeacherDashboardResponse.AttendanceByLessonRow> attendanceRows = lessons.stream()
                .map(l -> {
                    List<Attendance> list = attByLesson.getOrDefault(l.getId(), List.of());

                    long present = list.stream().filter(a -> a.getStatus().name().equals("PRESENT")).count();
                    long absent = list.stream().filter(a -> a.getStatus().name().equals("ABSENT")).count();
                    long late = list.stream().filter(a -> a.getStatus().name().equals("LATE")).count();
                    long total = list.size();

                    double pct = total == 0 ? 0 : ((double) present / total) * 100;

                    TeachingAssignment a = l.getTeachingAssignment();

                    return TeacherDashboardResponse.AttendanceByLessonRow.builder()
                            .lessonId(l.getId())
                            .date(String.valueOf(l.getDate()))
                            .assignmentId(a.getId())
                            .subjectName(a.getSubject().getName())
                            .groupName(a.getGroup().getName())
                            .total(total)
                            .present(present)
                            .absent(absent)
                            .late(late)
                            .participationPercent(round2(pct))
                            .build();
                })
                .sorted(Comparator.comparing(TeacherDashboardResponse.AttendanceByLessonRow::lessonId))
                .toList();

        // -------- At-risk students --------
        Map<Long, List<Grade>> gradesByStudent = grades.stream()
                .collect(Collectors.groupingBy(g -> g.getStudent().getId()));

        List<TeacherDashboardResponse.AtRiskStudentRow> atRisk = gradesByStudent.entrySet().stream()
                .map(e -> {
                    Long studentId = e.getKey();
                    List<Grade> list = e.getValue();
                    User student = list.get(0).getStudent();

                    double avgFinal = avg(list.stream().map(Grade::getFinalScore).toList());
                    long courses = list.size();

                    String level =
                            avgFinal < 60 ? "HIGH"
                                    : avgFinal < 70 ? "MEDIUM"
                                    : "OK";

                    return TeacherDashboardResponse.AtRiskStudentRow.builder()
                            .studentId(studentId)
                            .studentName(student.getFirstName() + " " + student.getLastName())
                            .groupName(student.getGroup() != null ? student.getGroup().getName() : null)
                            .avgFinalScore(round2(avgFinal))
                            .coursesCount(courses)
                            .riskLevel(level)
                            .build();
                })
                .filter(r -> !"OK".equals(r.riskLevel()))
                .sorted(Comparator
                        .comparing(TeacherDashboardResponse.AtRiskStudentRow::riskLevel).reversed()
                        .thenComparing(TeacherDashboardResponse.AtRiskStudentRow::avgFinalScore))
                .toList();

        return TeacherDashboardResponse.builder()
                .overview(TeacherDashboardResponse.Overview.builder()
                        .totalAssignments(totalAssignments)
                        .totalLessons(totalLessons)
                        .totalGrades(totalGrades)
                        .totalAttendanceRecords(totalAttendance)
                        .atRiskStudentsCount(atRisk.size())
                        .build())
                .gradesByAssignment(gradeRows)
                .attendanceByLesson(attendanceRows)
                .atRiskStudents(atRisk)
                .build();
    }

    private static long countLetter(List<Grade> list, String letter) {
        return list.stream().filter(g -> letter.equals(g.getLetterGrade())).count();
    }

    private static double avg(List<? extends Number> values) {
        if (values == null || values.isEmpty()) return 0;
        double s = 0;
        int n = 0;
        for (Number v : values) {
            if (v == null) continue;
            s += v.doubleValue();
            n++;
        }
        return n == 0 ? 0 : s / n;
    }

    private static double round2(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
