package az.mmc.sis.academic.department.service;

import az.mmc.sis.academic.department.dto.DepartmentRequest;
import az.mmc.sis.academic.department.dto.DepartmentResponse;
import az.mmc.sis.academic.department.mapper.DepartmentMapper;
import az.mmc.sis.academic.department.model.Department;
import az.mmc.sis.academic.department.repository.DepartmentRepository;
import az.mmc.sis.academic.faculty.model.Faculty;
import az.mmc.sis.academic.faculty.repository.FacultyRepository;
import az.mmc.sis.common.exception.ConflictException;
import az.mmc.sis.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository;

    @Transactional
    public DepartmentResponse create(DepartmentRequest request) {
        String name = request.name().trim();

        Faculty faculty = facultyRepository.findById(request.facultyId())
                .orElseThrow(() -> new NotFoundException("Faculty not found"));

        if (departmentRepository.existsByFacultyIdAndNameIgnoreCase(faculty.getId(), name)) {
            throw new ConflictException("Department with this name already exists in this faculty");
        }

        Department saved = departmentRepository.save(
                Department.builder()
                        .name(name)
                        .faculty(faculty)
                        .build()
        );

        return DepartmentMapper.toResponse(saved);
    }

    public List<DepartmentResponse> getAll() {
        return departmentRepository.findAll().stream()
                .map(DepartmentMapper::toResponse)
                .toList();
    }

    public List<DepartmentResponse> getAllByFacultyId(Long facultyId) {
        // validate faculty exists (nice UX for frontend)
        facultyRepository.findById(facultyId)
                .orElseThrow(() -> new NotFoundException("Faculty not found"));

        return departmentRepository.findAllByFacultyIdOrderByNameAsc(facultyId).stream()
                .map(DepartmentMapper::toResponse)
                .toList();
    }

    public DepartmentResponse getById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Department not found"));

        return DepartmentMapper.toResponse(department);
    }

    @Transactional
    public DepartmentResponse update(Long id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Department not found"));

        String name = request.name().trim();

        Faculty faculty = facultyRepository.findById(request.facultyId())
                .orElseThrow(() -> new NotFoundException("Faculty not found"));

        if (departmentRepository.existsByFacultyIdAndNameIgnoreCaseAndIdNot(faculty.getId(), name, id)) {
            throw new ConflictException("Department with this name already exists in this faculty");
        }

        department.setName(name);
        department.setFaculty(faculty);

        Department saved = departmentRepository.save(department);
        return DepartmentMapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Department not found"));

        try {
            departmentRepository.delete(department);
            departmentRepository.flush(); // force FK violations NOW (so frontend gets 409 immediately)
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("Department cannot be deleted because it is referenced by other records");
        }
    }
}
