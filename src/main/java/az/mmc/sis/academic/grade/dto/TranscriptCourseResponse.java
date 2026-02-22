package az.mmc.sis.academic.grade.dto;

import lombok.Builder;

@Builder
public record TranscriptCourseResponse(
        Long assignmentId,
        String subjectName,
        double finalScore,
        String letterGrade
) {}
