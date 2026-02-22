package az.mmc.sis.academic.term.controller;

import az.mmc.sis.academic.term.dto.TermRequest;
import az.mmc.sis.academic.term.dto.TermResponse;
import az.mmc.sis.academic.term.service.TermService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/terms")
@RequiredArgsConstructor
public class AdminTermController {

    private final TermService termService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public TermResponse create(@Valid @RequestBody TermRequest request) {
        return termService.create(request);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<TermResponse> list() {
        return termService.list();
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public TermResponse activate(@PathVariable Long id) {
        return termService.setActive(id);
    }

    @PostMapping("/{id}/lock")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public TermResponse lock(@PathVariable Long id) {
        return termService.setLocked(id, true);
    }

    @PostMapping("/{id}/unlock")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public TermResponse unlock(@PathVariable Long id) {
        return termService.setLocked(id, false);
    }
}
