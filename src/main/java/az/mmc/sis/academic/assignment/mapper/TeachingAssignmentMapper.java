package az.mmc.sis.academic.assignment.mapper;

import az.mmc.sis.academic.assignment.dto.TeachingAssignmentResponse;
import az.mmc.sis.academic.assignment.model.TeachingAssignment;

public final class TeachingAssignmentMapper {

    private TeachingAssignmentMapper() {}

    public static TeachingAssignmentResponse toResponse(TeachingAssignment a) {
        return TeachingAssignmentResponse.builder()
                .id(a.getId())
                .teacherId(a.getTeacher().getId())
                .teacherName(a.getTeacher().getFirstName() + " " + a.getTeacher().getLastName())
                .subjectId(a.getSubject().getId())
                .subjectName(a.getSubject().getName())
                .groupId(a.getGroup().getId())
                .groupName(a.getGroup().getName())
                .build();
    }
}
