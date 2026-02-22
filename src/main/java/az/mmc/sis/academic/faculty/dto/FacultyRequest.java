package az.mmc.sis.academic.faculty.dto;

import jakarta.validation.constraints.NotBlank;

public record FacultyRequest(
        @NotBlank(message = "name is required")
        String name
) {}