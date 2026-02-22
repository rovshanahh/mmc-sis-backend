package az.mmc.sis.academic.term.dto;

import lombok.Builder;

@Builder
public record TermResponse(
        Long id,
        String code,
        String name,
        boolean active,
        boolean locked
) {}
