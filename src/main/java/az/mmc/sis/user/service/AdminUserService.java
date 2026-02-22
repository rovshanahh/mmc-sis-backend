package az.mmc.sis.user.service;

import az.mmc.sis.user.dto.CreateUserRequest;
import az.mmc.sis.user.dto.UserResponse;
import az.mmc.sis.user.model.User;
import az.mmc.sis.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import az.mmc.sis.academic.group.model.StudentGroup;
import az.mmc.sis.academic.group.repository.StudentGroupRepository;
import az.mmc.sis.common.exception.BadRequestException;
import az.mmc.sis.common.exception.ForbiddenException;
import az.mmc.sis.common.exception.NotFoundException;
import az.mmc.sis.user.model.Role;


@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StudentGroupRepository studentGroupRepository;

    public void createUser(CreateUserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }
    
        StudentGroup group = null;
    
        if (request.getRole() == Role.STUDENT) {
    
            if (request.getGroupId() == null) {
                throw new ForbiddenException("Student must be assigned to a group");
            }
    
            group = studentGroupRepository.findById(request.getGroupId())
                    .orElseThrow(() -> new NotFoundException("Group not found"));
        }
    
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .group(group)
                .build();
    
        userRepository.save(user);
    }

    public List<UserResponse> listUsers(String role) {

        List<User> users;

        if (role != null) {
            users = userRepository.findByRole(Role.valueOf(role));
        } else {
            users = userRepository.findAll();
        }

        return users.stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .groupId(user.getGroup() != null ? user.getGroup().getId() : null)
                        .build())
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        userRepository.delete(user);
    }
}
