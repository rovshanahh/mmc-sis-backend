package az.mmc.sis.academic.term.mapper;

import az.mmc.sis.academic.term.dto.TermResponse;
import az.mmc.sis.academic.term.model.Term;

public class TermMapper {
    public static TermResponse toResponse(Term t) {
        return TermResponse.builder()
                .id(t.getId())
                .code(t.getCode())
                .name(t.getName())
                .active(t.isActive())
                .locked(t.isLocked())
                .build();
    }
}
