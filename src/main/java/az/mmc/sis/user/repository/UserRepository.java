package az.mmc.sis.user.repository;

import az.mmc.sis.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import az.mmc.sis.user.model.Role;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByGroupId(Long groupId);

    List<User> findByRole(Role role);
}
