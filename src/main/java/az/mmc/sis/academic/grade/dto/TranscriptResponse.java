package az.mmc.sis.academic.grade.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record TranscriptResponse(
        Long studentId,
        String studentName,
        String groupName,
        double gpa,
        int totalCourses,
        List<TranscriptCourseResponse> courses
) {}
