package az.mmc.sis.config;

import az.mmc.sis.user.model.Role;
import az.mmc.sis.user.model.User;
import az.mmc.sis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.existsByEmail("admin@mmc.local")) return;

        User admin = User.builder()
                .firstName("System")
                .lastName("Admin")
                .email("admin@mmc.local")
                .password(passwordEncoder.encode("Admin123!"))
                .role(Role.ADMIN)
                .build();

        userRepository.save(admin);
    }
}
