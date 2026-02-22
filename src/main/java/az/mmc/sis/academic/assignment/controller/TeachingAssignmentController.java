package az.mmc.sis.academic.assignment.controller;

import az.mmc.sis.academic.assignment.dto.TeachingAssignmentRequest;
import az.mmc.sis.academic.assignment.dto.TeachingAssignmentResponse;
import az.mmc.sis.academic.assignment.service.TeachingAssignmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/assignments")
@RequiredArgsConstructor
public class TeachingAssignmentController {

    private final TeachingAssignmentService teachingAssignmentService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public TeachingAssignmentResponse assignTeacher(@Valid @RequestBody TeachingAssignmentRequest request) {
        return teachingAssignmentService.assign(request);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<TeachingAssignmentResponse> getAllAssignments(
            @RequestParam(required = false) Long teacherId,
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false) Long groupId
    ) {
        return teachingAssignmentService.getAll(teacherId, subjectId, groupId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public TeachingAssignmentResponse getAssignmentById(@PathVariable Long id) {
        return teachingAssignmentService.getById(id);
    }
}