package az.mmc.sis.academic.grade.controller;

import az.mmc.sis.academic.grade.dto.GradeRequest;
import az.mmc.sis.academic.grade.dto.GradeResponse;
import az.mmc.sis.academic.grade.service.GradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/teacher/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public GradeResponse assignGrade(@Valid @RequestBody GradeRequest request, Authentication authentication) {
        return gradeService.upsertGrade(request, authentication.getName());
    }
}