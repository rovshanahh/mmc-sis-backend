package az.mmc.sis.academic.department.mapper;

import az.mmc.sis.academic.department.dto.DepartmentResponse;
import az.mmc.sis.academic.department.model.Department;

public final class DepartmentMapper {

    private DepartmentMapper() {}

    public static DepartmentResponse toResponse(Department d) {
        return new DepartmentResponse(
                d.getId(),
                d.getName(),
                d.getFaculty().getId(),
                d.getFaculty().getName()
        );
    }
}
