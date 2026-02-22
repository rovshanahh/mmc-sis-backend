package az.mmc.sis.academic.term.repository;

import az.mmc.sis.academic.term.model.Term;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TermRepository extends JpaRepository<Term, Long> {
    boolean existsByCodeIgnoreCase(String code);
    Optional<Term> findByActiveTrue();
}
