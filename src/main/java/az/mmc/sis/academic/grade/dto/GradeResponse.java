package az.mmc.sis.academic.grade.dto;

import lombok.Builder;

@Builder
public record GradeResponse(
        Long id,
        Long studentId,
        String studentName,
        Long assignmentId,
        String subjectName,
        Double quiz,
        Double midterm,
        Double finalExam,
        Double finalScore,
        String letterGrade
) {}
