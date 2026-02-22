package az.mmc.sis.academic.group.model;

import az.mmc.sis.academic.department.model.Department;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "student_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "department_id")
    private Department department;
}
