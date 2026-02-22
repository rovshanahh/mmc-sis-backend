package az.mmc.sis.academic.lesson.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class LessonResponse {

    private Long id;
    private LocalDate date;

    private Long assignmentId;
    private String teacherName;
    private String subjectName;
    private String groupName;
}
