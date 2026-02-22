package az.mmc.sis.academic.department.dto;

public record DepartmentResponse(
        Long id,
        String name,
        Long facultyId,
        String facultyName
) {}
