package az.mmc.sis.academic.lesson.model;

import az.mmc.sis.academic.assignment.model.TeachingAssignment;
import az.mmc.sis.academic.attendance.model.Attendance;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lessons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(optional = false)
    @JoinColumn(name = "assignment_id")
    private TeachingAssignment teachingAssignment;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL)
    private List<Attendance> attendances = new ArrayList<>();
}
