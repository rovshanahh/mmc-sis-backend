package az.mmc.sis.academic.group.mapper;

import az.mmc.sis.academic.group.dto.StudentGroupResponse;
import az.mmc.sis.academic.group.model.StudentGroup;

public final class StudentGroupMapper {

    private StudentGroupMapper() {}

    public static StudentGroupResponse toResponse(StudentGroup g) {
        return StudentGroupResponse.builder()
                .id(g.getId())
                .name(g.getName())
                .departmentId(g.getDepartment().getId())
                .departmentName(g.getDepartment().getName())
                .build();
    }
}
