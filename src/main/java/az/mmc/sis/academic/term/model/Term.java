package az.mmc.sis.academic.term.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "terms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Term {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // e.g. "2025-FALL"

    @Column(nullable = false)
    private String name; // e.g. "Fall 2025"

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private boolean locked;
}
