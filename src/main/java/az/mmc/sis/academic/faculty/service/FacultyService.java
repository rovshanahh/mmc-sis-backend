package az.mmc.sis.academic.faculty.service;

import az.mmc.sis.academic.faculty.dto.FacultyRequest;
import az.mmc.sis.academic.faculty.dto.FacultyResponse;
import az.mmc.sis.academic.faculty.mapper.FacultyMapper;
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
public class FacultyService {

    private final FacultyRepository facultyRepository;

    @Transactional
    public FacultyResponse create(FacultyRequest request) {
        String name = request.name().trim();

        if (facultyRepository.existsByNameIgnoreCase(name)) {
            throw new ConflictException("Faculty with this name already exists");
        }

        Faculty saved = facultyRepository.save(
                Faculty.builder().name(name).build()
        );

        return FacultyMapper.toResponse(saved);
    }

    public List<FacultyResponse> getAll() {
        return facultyRepository.findAll()
                .stream()
                .map(FacultyMapper::toResponse)
                .toList();
    }

    public FacultyResponse getById(Long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Faculty not found"));

        return FacultyMapper.toResponse(faculty);
    }

    @Transactional
    public FacultyResponse update(Long id, FacultyRequest request) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Faculty not found"));

        String name = request.name().trim();

        if (facultyRepository.existsByNameIgnoreCaseAndIdNot(name, id)) {
            throw new ConflictException("Faculty with this name already exists");
        }

        faculty.setName(name);
        return FacultyMapper.toResponse(faculty);
    }

    @Transactional
    public void delete(Long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Faculty not found"));

        try {
            facultyRepository.delete(faculty);
            facultyRepository.flush(); // force FK check now (inside request)
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException(
                    "Cannot delete faculty because it has departments. Delete departments first."
            );
        }
    }
}
