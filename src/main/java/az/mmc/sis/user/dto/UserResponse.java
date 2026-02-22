package az.mmc.sis.user.dto;

import az.mmc.sis.user.model.Role;
import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Role role,
        Long groupId
) {}
