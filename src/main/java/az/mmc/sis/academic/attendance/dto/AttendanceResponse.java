package az.mmc.sis.academic.attendance.dto;

import lombok.Builder;

@Builder
public record AttendanceResponse(
        Long id,
        Long lessonId,
        Long studentId,
        String studentName,
        String status
) {}
