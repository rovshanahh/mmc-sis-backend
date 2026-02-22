package az.mmc.sis.academic.faculty.mapper;

import az.mmc.sis.academic.faculty.dto.FacultyResponse;
import az.mmc.sis.academic.faculty.model.Faculty;

public final class FacultyMapper {

    private FacultyMapper() {}

    public static FacultyResponse toResponse(Faculty faculty) {
        return new FacultyResponse(
                faculty.getId(),
                faculty.getName()
        );
    }
}
