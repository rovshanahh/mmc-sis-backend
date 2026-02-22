package az.mmc.sis.academic.assignment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeachingAssignmentRequest {

    @NotNull(message = "teacherId is required")
    private Long teacherId;

    @NotNull(message = "subjectId is required")
    private Long subjectId;

    @NotNull(message = "groupId is required")
    private Long groupId;
}
