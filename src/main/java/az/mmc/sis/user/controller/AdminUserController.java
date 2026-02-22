package az.mmc.sis.user.controller;

import az.mmc.sis.user.dto.CreateUserRequest;
import az.mmc.sis.user.dto.UserResponse;
import az.mmc.sis.user.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void createUser(@Valid @RequestBody CreateUserRequest request) {
        adminUserService.createUser(request);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<UserResponse> listUsers(
            @RequestParam(required = false) String role
    ) {
        return adminUserService.listUsers(role);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteUser(@PathVariable Long id) {
        adminUserService.delete(id);
    }
}
