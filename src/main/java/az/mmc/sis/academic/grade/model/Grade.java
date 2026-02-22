package az.mmc.sis.academic.grade.model;

import az.mmc.sis.academic.assignment.model.TeachingAssignment;
import az.mmc.sis.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "grades",
       uniqueConstraints = @UniqueConstraint(
               columnNames = {"student_id", "teaching_assignment_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User student;

    @ManyToOne(optional = false)
    private TeachingAssignment teachingAssignment;

    private Double quiz;
    private Double midterm;
    private Double finalExam;

    private Double finalScore;
    private String letterGrade;
}
