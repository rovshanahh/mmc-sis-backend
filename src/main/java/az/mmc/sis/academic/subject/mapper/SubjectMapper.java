package az.mmc.sis.academic.subject.mapper;

import az.mmc.sis.academic.subject.dto.SubjectResponse;
import az.mmc.sis.academic.subject.model.Subject;

public final class SubjectMapper {

    private SubjectMapper() {}

    public static SubjectResponse toResponse(Subject s) {
        return SubjectResponse.builder()
                .id(s.getId())
                .name(s.getName())
                .credit(s.getCredit())
                .departmentId(s.getDepartment().getId())
                .departmentName(s.getDepartment().getName())
                .build();
    }
}
