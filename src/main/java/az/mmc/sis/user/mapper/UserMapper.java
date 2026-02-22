package az.mmc.sis.user.mapper;

import az.mmc.sis.user.dto.UserResponse;
import az.mmc.sis.user.model.User;

public class UserMapper {

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .groupId(user.getGroup() != null ? user.getGroup().getId() : null)
                .build();
    }
}
