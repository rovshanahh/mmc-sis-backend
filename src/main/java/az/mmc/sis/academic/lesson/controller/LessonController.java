package az.mmc.sis.academic.lesson.controller;

import az.mmc.sis.academic.attendance.dto.AttendanceRequest;
import az.mmc.sis.academic.attendance.dto.LessonStudentResponse;
import az.mmc.sis.academic.lesson.dto.LessonRequest;
import az.mmc.sis.academic.lesson.dto.LessonResponse;
import az.mmc.sis.academic.lesson.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teacher/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public LessonResponse createLesson(@Valid @RequestBody LessonRequest request, Authentication authentication) {
        return lessonService.createLesson(request, authentication.getName());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public List<LessonResponse> getMyLessons(Authentication authentication) {
        return lessonService.getMyLessons(authentication.getName());
    }

    @GetMapping("/{lessonId}/students")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public List<LessonStudentResponse> getStudentsForLesson(@PathVariable Long lessonId, Authentication authentication) {
        return lessonService.getStudentsForLesson(lessonId, authentication.getName());
    }

    @PostMapping("/{lessonId}/attendance")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public void recordAttendance(
            @PathVariable Long lessonId,
            @Valid @RequestBody List<AttendanceRequest> requests,
            Authentication authentication) {

        lessonService.recordAttendance(lessonId, requests, authentication.getName());
    }
}