package az.mmc.sis.academic.attendance.controller;

import az.mmc.sis.academic.attendance.dto.AttendanceCreateRequest;
import az.mmc.sis.academic.attendance.dto.AttendanceResponse;
import az.mmc.sis.academic.attendance.service.AttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/teacher/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_TEACHER')")
    public AttendanceResponse recordAttendance(
            @Valid @RequestBody AttendanceCreateRequest request,
            Authentication authentication) {

        return attendanceService.recordAttendance(request, authentication.getName());
    }
}
