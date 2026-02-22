package az.mmc.sis.academic.subject.service;

import az.mmc.sis.academic.department.model.Department;
import az.mmc.sis.academic.department.repository.DepartmentRepository;
import az.mmc.sis.academic.subject.dto.SubjectRequest;
import az.mmc.sis.academic.subject.dto.SubjectResponse;
import az.mmc.sis.academic.subject.mapper.SubjectMapper;
import az.mmc.sis.academic.subject.model.Subject;
import az.mmc.sis.academic.subject.repository.SubjectRepository;
import az.mmc.sis.common.exception.ConflictException;
import az.mmc.sis.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final DepartmentRepository departmentRepository;

    @Transactional
    public SubjectResponse create(SubjectRequest request) {
        String name = request.getName().trim();

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new NotFoundException("Department not found"));

        if (subjectRepository.existsByDepartmentIdAndNameIgnoreCase(department.getId(), name)) {
            throw new ConflictException("Subject with this name already exists in this department");
        }

        Subject saved = subjectRepository.save(
                Subject.builder()
                        .name(name)
                        .credit(request.getCredit())
                        .department(department)
                        .build()
        );

        return SubjectMapper.toResponse(saved);
    }

    public List<SubjectResponse> getAll() {
        return subjectRepository.findAll()
                .stream()
                .map(SubjectMapper::toResponse)
                .toList();
    }

    public SubjectResponse getById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subject not found"));

        return SubjectMapper.toResponse(subject);
    }

    @Transactional
    public SubjectResponse update(Long id, SubjectRequest request) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subject not found"));

        String name = request.getName().trim();

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new NotFoundException("Department not found"));

        if (subjectRepository.existsByDepartmentIdAndNameIgnoreCaseAndIdNot(department.getId(), name, id)) {
            throw new ConflictException("Subject with this name already exists in this department");
        }

        subject.setName(name);
        subject.setCredit(request.getCredit());
        subject.setDepartment(department);

        Subject saved = subjectRepository.save(subject);
        return SubjectMapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Subject not found"));

        subjectRepository.delete(subject);
    }
}
