package az.mmc.sis.academic.grade.controller;

import az.mmc.sis.academic.grade.dto.AcademicSummaryResponse;
import az.mmc.sis.academic.grade.dto.StudentGpaResponse;
import az.mmc.sis.academic.grade.dto.StudentGradeResponse;
import az.mmc.sis.academic.grade.dto.TranscriptResponse;
import az.mmc.sis.academic.grade.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student/grades")
@RequiredArgsConstructor
public class StudentGradeController {

    private final GradeService gradeService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public List<StudentGradeResponse> getMyGrades(Authentication authentication) {
        return gradeService.getMyGrades(authentication.getName());
    }

    @GetMapping("/gpa")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public StudentGpaResponse calculateGpa(Authentication authentication) {
        return gradeService.getMyGpa(authentication.getName());
    }

    @GetMapping("/transcript")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public TranscriptResponse getTranscript(Authentication authentication) {
        return gradeService.getMyTranscript(authentication.getName());
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public AcademicSummaryResponse getMyAcademicSummary(Authentication authentication) {
        return gradeService.getAcademicSummary(authentication.getName());
    }
}