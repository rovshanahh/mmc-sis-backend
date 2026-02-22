package az.mmc.sis.academic.lesson.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record LessonRequest(
        @NotNull(message = "assignmentId is required")
        Long assignmentId,

        @NotNull(message = "date is required")
        LocalDate date
) {}
