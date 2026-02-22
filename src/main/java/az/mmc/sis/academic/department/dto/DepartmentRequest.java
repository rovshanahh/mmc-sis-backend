package az.mmc.sis.academic.department.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DepartmentRequest(
        @NotBlank(message = "name is required")
        String name,

        @NotNull(message = "facultyId is required")
        Long facultyId
) {}
