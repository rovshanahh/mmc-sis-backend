package az.mmc.sis.academic.faculty.controller;

import az.mmc.sis.academic.faculty.dto.FacultyRequest;
import az.mmc.sis.academic.faculty.dto.FacultyResponse;
import az.mmc.sis.academic.faculty.service.FacultyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/faculties")
@RequiredArgsConstructor
public class FacultyController {

    private final FacultyService facultyService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public FacultyResponse createFaculty(@Valid @RequestBody FacultyRequest request) {
        return facultyService.create(request);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<FacultyResponse> getAllFaculties() {
        return facultyService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public FacultyResponse getFacultyById(@PathVariable Long id) {
        return facultyService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public FacultyResponse updateFaculty(@PathVariable Long id,
                                         @Valid @RequestBody FacultyRequest request) {
        return facultyService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteFaculty(@PathVariable Long id) {
        facultyService.delete(id);
    }
}
