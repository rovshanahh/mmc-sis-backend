package az.mmc.sis.academic.attendance.controller;

import az.mmc.sis.academic.attendance.dto.StudentAttendanceSummaryResponse;
import az.mmc.sis.academic.attendance.service.AttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/student/attendance")
@RequiredArgsConstructor
public class StudentAttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_STUDENT')")
    public StudentAttendanceSummaryResponse getMyAttendance(Authentication authentication) {
        return attendanceService.getMyAttendance(authentication.getName());
    }
}
