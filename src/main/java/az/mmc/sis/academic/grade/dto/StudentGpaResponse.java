package az.mmc.sis.academic.grade.dto;

import lombok.Builder;

@Builder
public record StudentGpaResponse(
        double gpa,
        int totalCourses
) {}
