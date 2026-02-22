package az.mmc.sis.academic.analytics.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record TeacherDashboardResponse(
        Overview overview,
        List<GradeByAssignmentRow> gradesByAssignment,
        List<AttendanceByLessonRow> attendanceByLesson,
        List<AtRiskStudentRow> atRiskStudents
) {
    @Builder
    public record Overview(
            long totalAssignments,
            long totalLessons,
            long totalGrades,
            long totalAttendanceRecords,
            long atRiskStudentsCount
    ) {}

    @Builder
    public record GradeByAssignmentRow(
            Long assignmentId,
            String subjectName,
            String groupName,
            int credit,
            long gradedCount,
            double avgFinalScore,
            double avgQuiz,
            double avgMidterm,
            double avgFinalExam,
            long aCount,
            long bCount,
            long cCount,
            long dCount,
            long fCount
    ) {}

    @Builder
    public record AttendanceByLessonRow(
            Long lessonId,
            String date,
            Long assignmentId,
            String subjectName,
            String groupName,
            long total,
            long present,
            long absent,
            long late,
            double participationPercent
    ) {}

    @Builder
    public record AtRiskStudentRow(
            Long studentId,
            String studentName,
            String groupName,
            double avgFinalScore,
            long coursesCount,
            String riskLevel
    ) {}
}
