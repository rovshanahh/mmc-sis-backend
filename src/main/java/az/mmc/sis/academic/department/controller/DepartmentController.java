package az.mmc.sis.academic.department.controller;

import az.mmc.sis.academic.department.dto.DepartmentRequest;
import az.mmc.sis.academic.department.dto.DepartmentResponse;
import az.mmc.sis.academic.department.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public DepartmentResponse createDepartment(@Valid @RequestBody DepartmentRequest request) {
        return departmentService.create(request);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<DepartmentResponse> getAllDepartments(
            @RequestParam(required = false) Long facultyId
    ) {
        return (facultyId == null)
                ? departmentService.getAll()
                : departmentService.getAllByFacultyId(facultyId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public DepartmentResponse getDepartmentById(@PathVariable Long id) {
        return departmentService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public DepartmentResponse updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequest request
    ) {
        return departmentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteDepartment(@PathVariable Long id) {
        departmentService.delete(id);
    }
}
