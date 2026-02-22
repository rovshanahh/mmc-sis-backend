package az.mmc.sis.academic.grade.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record GradeRequest(

        @NotNull(message = "studentId is required")
        Long studentId,

        @NotNull(message = "assignmentId is required")
        Long assignmentId,

        @NotNull(message = "quiz is required")
        @Min(value = 0, message = "quiz must be at least 0")
        @Max(value = 100, message = "quiz must be at most 100")
        Double quiz,

        @NotNull(message = "midterm is required")
        @Min(value = 0, message = "midterm must be at least 0")
        @Max(value = 100, message = "midterm must be at most 100")
        Double midterm,

        @NotNull(message = "finalExam is required")
        @Min(value = 0, message = "finalExam must be at least 0")
        @Max(value = 100, message = "finalExam must be at most 100")
        Double finalExam
) {}
