package az.mmc.sis.academic.term.controller;

import az.mmc.sis.academic.term.dto.TermResponse;
import az.mmc.sis.academic.term.service.TermService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/terms")
@RequiredArgsConstructor
public class TermController {

    private final TermService termService;

    @GetMapping("/active")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_TEACHER','ROLE_STUDENT')")
    public TermResponse active() {
        return termService.getActive();
    }
}
