package az.mmc.sis.academic.group.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StudentGroupRequest {

    @NotBlank(message = "name is required")
    private String name;

    @NotNull(message = "departmentId is required")
    private Long departmentId;
}
