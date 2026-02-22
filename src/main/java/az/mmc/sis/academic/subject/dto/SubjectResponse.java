package az.mmc.sis.academic.subject.dto;

import lombok.Builder;

@Builder
public record SubjectResponse(
        Long id,
        String name,
        Integer credit,
        Long departmentId,
        String departmentName
) {}
