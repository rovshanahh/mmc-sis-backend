package az.mmc.sis.academic.group.controller;

import az.mmc.sis.academic.group.dto.StudentGroupRequest;
import az.mmc.sis.academic.group.dto.StudentGroupResponse;
import az.mmc.sis.academic.group.service.StudentGroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/groups")
@RequiredArgsConstructor
public class StudentGroupController {

    private final StudentGroupService studentGroupService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public StudentGroupResponse createGroup(@Valid @RequestBody StudentGroupRequest request) {
        return studentGroupService.create(request);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<StudentGroupResponse> getAllGroups() {
        return studentGroupService.getAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public StudentGroupResponse getGroupById(@PathVariable Long id) {
        return studentGroupService.getById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteGroup(@PathVariable Long id) {
        studentGroupService.delete(id);
    }
}
