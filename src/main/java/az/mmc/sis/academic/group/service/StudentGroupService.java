package az.mmc.sis.academic.group.service;

import az.mmc.sis.academic.department.model.Department;
import az.mmc.sis.academic.department.repository.DepartmentRepository;
import az.mmc.sis.academic.group.dto.StudentGroupRequest;
import az.mmc.sis.academic.group.dto.StudentGroupResponse;
import az.mmc.sis.academic.group.mapper.StudentGroupMapper;
import az.mmc.sis.academic.group.model.StudentGroup;
import az.mmc.sis.academic.group.repository.StudentGroupRepository;
import az.mmc.sis.common.exception.ConflictException;
import az.mmc.sis.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudentGroupService {

    private final StudentGroupRepository studentGroupRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public StudentGroupResponse create(StudentGroupRequest request) {
        String name = request.getName().trim();

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new NotFoundException("Department not found"));

        if (studentGroupRepository.existsByDepartmentIdAndNameIgnoreCase(department.getId(), name)) {
            throw new ConflictException("Group with this name already exists in this department");
        }

        StudentGroup saved = studentGroupRepository.save(
                StudentGroup.builder()
                        .name(name)
                        .department(department)
                        .build()
        );

        return StudentGroupMapper.toResponse(saved);
    }

    public List<StudentGroupResponse> getAll() {
        return studentGroupRepository.findAll()
                .stream()
                .map(StudentGroupMapper::toResponse)
                .toList();
    }

    public StudentGroupResponse getById(Long id) {
        StudentGroup group = studentGroupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Group not found"));

        return StudentGroupMapper.toResponse(group);
    }

    @Transactional
    public void delete(Long id) {
        StudentGroup group = studentGroupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Group not found"));

        studentGroupRepository.delete(group);
    }
}
