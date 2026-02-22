package az.mmc.sis.academic.attendance.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record StudentAttendanceSummaryResponse(
        List<StudentAttendanceResponse> records,
        int totalLessons,
        int presentCount,
        double participationPercentage
) {}
