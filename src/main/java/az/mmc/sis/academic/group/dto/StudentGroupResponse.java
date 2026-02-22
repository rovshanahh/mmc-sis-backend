package az.mmc.sis.academic.group.dto;

import lombok.Builder;

@Builder
public record StudentGroupResponse(
        Long id,
        String name,
        Long departmentId,
        String departmentName
) { }
