package az.mmc.sis.academic.subject.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SubjectRequest {

    @NotBlank(message = "name is required")
    @Size(max = 100, message = "name must be at most 100 characters")
    private String name;

    @NotNull(message = "credit is required")
    @Min(value = 1, message = "credit must be at least 1")
    @Max(value = 30, message = "credit must be at most 30")
    private Integer credit;

    @NotNull(message = "departmentId is required")
    private Long departmentId;
}
