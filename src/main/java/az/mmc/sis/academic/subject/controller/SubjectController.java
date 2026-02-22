package az.mmc.sis.academic.subject.controller;

import az.mmc.sis.academic.subject.dto.SubjectRequest;
import az.mmc.sis.academic.subject.dto.SubjectResponse;
import az.mmc.sis.academic.subject.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public SubjectResponse createSubject(@Valid @RequestBody SubjectRequest request) {
        return subjectService.create(request);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<SubjectResponse> getAllSubjects() {
        return subjectService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public SubjectResponse getSubjectById(@PathVariable Long id) {
        return subjectService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public SubjectResponse updateSubject(@PathVariable Long id, @Valid @RequestBody SubjectRequest request) {
        return subjectService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteSubject(@PathVariable Long id) {
        subjectService.delete(id);
    }
}
