package az.mmc.sis.academic.lesson.mapper;

import az.mmc.sis.academic.lesson.dto.LessonResponse;
import az.mmc.sis.academic.lesson.model.Lesson;

public final class LessonMapper {

    private LessonMapper() {}

    public static LessonResponse toResponse(Lesson lesson) {
        return LessonResponse.builder()
                .id(lesson.getId())
                .date(lesson.getDate())
                .assignmentId(lesson.getTeachingAssignment().getId())
                .teacherName(
                        lesson.getTeachingAssignment().getTeacher().getFirstName()
                                + " " + lesson.getTeachingAssignment().getTeacher().getLastName()
                )
                .subjectName(lesson.getTeachingAssignment().getSubject().getName())
                .groupName(lesson.getTeachingAssignment().getGroup().getName())
                .build();
    }
}
