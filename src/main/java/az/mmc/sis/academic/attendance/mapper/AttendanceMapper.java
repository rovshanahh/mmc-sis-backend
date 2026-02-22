package az.mmc.sis.academic.attendance.mapper;

import az.mmc.sis.academic.attendance.dto.AttendanceResponse;
import az.mmc.sis.academic.attendance.dto.StudentAttendanceResponse;
import az.mmc.sis.academic.attendance.model.Attendance;

public final class AttendanceMapper {

    private AttendanceMapper() {}

    public static AttendanceResponse toTeacherResponse(Attendance a) {
        return AttendanceResponse.builder()
                .id(a.getId())
                .lessonId(a.getLesson().getId())
                .studentId(a.getStudent().getId())
                .studentName(a.getStudent().getFirstName() + " " + a.getStudent().getLastName())
                .status(a.getStatus().name())
                .build();
    }

    public static StudentAttendanceResponse toStudentRecord(Attendance a) {
        return StudentAttendanceResponse.builder()
                .lessonId(a.getLesson().getId())
                .date(a.getLesson().getDate())
                .subjectName(a.getLesson().getTeachingAssignment().getSubject().getName())
                .status(a.getStatus().name())
                .build();
    }
}
