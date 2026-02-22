package az.mmc.sis.academic.attendance.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record StudentAttendanceResponse(
        Long lessonId,
        LocalDate date,
        String subjectName,
        String status
) {}
