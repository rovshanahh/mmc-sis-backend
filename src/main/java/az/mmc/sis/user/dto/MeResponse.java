package az.mmc.sis.user.dto;

import az.mmc.sis.user.model.Role;

public record MeResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        Role role,
        Long groupId,
        String groupName
) {}
