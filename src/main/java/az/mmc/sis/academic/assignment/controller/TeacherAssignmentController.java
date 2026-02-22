package az.mmc.sis.academic.assignment.controller;

import az.mmc.sis.academic.assignment.dto.TeachingAssignmentResponse;
import az.mmc.sis.academic.assignment.service.TeachingAssignmentService;
import az.mmc.sis.academic.attendance.dto.LessonStudentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teacher/assignments")
@RequiredArgsConstructor
public class TeacherAssignmentController {

    private final TeachingAssignmentService teachingAssignmentService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public List<TeachingAssignmentResponse> getMyAssignments(Authentication authentication) {
        return teachingAssignmentService.getMyAssignments(authentication.getName());
    }

    @GetMapping("/{assignmentId}/students")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public List<LessonStudentResponse> getStudentsForAssignment(
            @PathVariable Long assignmentId,
            Authentication authentication
    ) {
        return teachingAssignmentService.getStudentsForAssignment(assignmentId, authentication.getName());
    }
}
