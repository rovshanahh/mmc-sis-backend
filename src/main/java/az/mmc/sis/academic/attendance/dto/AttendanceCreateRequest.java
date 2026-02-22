package az.mmc.sis.academic.attendance.dto;

import az.mmc.sis.academic.attendance.model.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

public record AttendanceCreateRequest(

        @NotNull(message = "lessonId is required")
        Long lessonId,

        @NotNull(message = "studentId is required")
        Long studentId,

        @NotNull(message = "status is required")
        AttendanceStatus status

) {}
