package az.mmc.sis.auth.controller;

import az.mmc.sis.auth.dto.AuthResponse;
import az.mmc.sis.auth.dto.LoginRequest;
import az.mmc.sis.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}
