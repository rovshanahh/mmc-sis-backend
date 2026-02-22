package az.mmc.sis.academic.attendance.dto;

import az.mmc.sis.academic.attendance.model.AttendanceStatus;
import jakarta.validation.constraints.NotNull;

public record AttendanceRequest(
        @NotNull Long studentId,
        @NotNull AttendanceStatus status
) {}
