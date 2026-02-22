package az.mmc.sis.academic.attendance.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LessonStudentResponse {
    private Long studentId;
    private String studentName;
}