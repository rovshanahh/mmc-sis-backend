package az.mmc.sis.academic.grade.dto;

import lombok.Builder;

@Builder
public record AcademicSummaryResponse(
        Double gpa,
        int totalCourses,
        int totalCreditsAttempted,
        int totalCreditsPassed,
        int failedCourses
) {}
