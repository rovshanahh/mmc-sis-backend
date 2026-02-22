package az.mmc.sis.academic.analytics.controller;

import az.mmc.sis.academic.analytics.dto.TeacherDashboardResponse;
import az.mmc.sis.academic.analytics.service.TeacherAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/teacher/analytics")
@RequiredArgsConstructor
public class TeacherAnalyticsController {

    private final TeacherAnalyticsService teacherAnalyticsService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public TeacherDashboardResponse dashboard(Authentication authentication) {
        return teacherAnalyticsService.getDashboard(authentication.getName());
    }
}





