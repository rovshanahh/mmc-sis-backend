package az.mmc.sis.academic.assignment.model;

import az.mmc.sis.academic.group.model.StudentGroup;
import az.mmc.sis.academic.subject.model.Subject;
import az.mmc.sis.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "teaching_assignments",
       uniqueConstraints = @UniqueConstraint(columnNames = {
               "teacher_id", "subject_id", "group_id"
       }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeachingAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "teacher_id")
    private User teacher; 

    @ManyToOne(optional = false)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(optional = false)
    @JoinColumn(name = "group_id")
    private StudentGroup group;
}
