package az.mmc.sis.academic.attendance.model;

import az.mmc.sis.academic.lesson.model.Lesson;
import az.mmc.sis.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attendance",
       uniqueConstraints = @UniqueConstraint(columnNames = {
               "lesson_id", "student_id"
       }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private User student; // must have role STUDENT

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;
}
