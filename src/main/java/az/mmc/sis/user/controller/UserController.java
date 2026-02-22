package az.mmc.sis.user.controller;

import az.mmc.sis.common.exception.NotFoundException;
import az.mmc.sis.user.dto.MeResponse;
import az.mmc.sis.user.model.User;
import az.mmc.sis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public MeResponse me(Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Long groupId = user.getGroup() == null ? null : user.getGroup().getId();
        String groupName = user.getGroup() == null ? null : user.getGroup().getName();

        return new MeResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                groupId,
                groupName
        );
    }
}
