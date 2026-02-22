package az.mmc.sis.academic.term.service;

import az.mmc.sis.academic.term.dto.TermRequest;
import az.mmc.sis.academic.term.dto.TermResponse;
import az.mmc.sis.academic.term.mapper.TermMapper;
import az.mmc.sis.academic.term.model.Term;
import az.mmc.sis.academic.term.repository.TermRepository;
import az.mmc.sis.common.exception.ConflictException;
import az.mmc.sis.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TermService {

    private final TermRepository termRepository;

    @Transactional
    public TermResponse create(TermRequest request) {
        String code = request.getCode().trim();
        String name = request.getName().trim();

        if (termRepository.existsByCodeIgnoreCase(code)) {
            throw new ConflictException("Term code already exists");
        }

        Term saved = termRepository.save(
                Term.builder()
                        .code(code)
                        .name(name)
                        .active(false)
                        .locked(false)
                        .build()
        );

        return TermMapper.toResponse(saved);
    }

    public List<TermResponse> list() {
        return termRepository.findAll().stream()
                .map(TermMapper::toResponse)
                .toList();
    }

    public TermResponse getActive() {
        Term t = termRepository.findByActiveTrue()
                .orElseThrow(() -> new NotFoundException("No active term"));
        return TermMapper.toResponse(t);
    }

    @Transactional
    public TermResponse setActive(Long termId) {
        Term target = termRepository.findById(termId)
                .orElseThrow(() -> new NotFoundException("Term not found"));

        // deactivate current active
        termRepository.findByActiveTrue().ifPresent(t -> {
            t.setActive(false);
            termRepository.save(t);
        });

        target.setActive(true);
        return TermMapper.toResponse(termRepository.save(target));
    }

    @Transactional
    public TermResponse setLocked(Long termId, boolean locked) {
        Term t = termRepository.findById(termId)
                .orElseThrow(() -> new NotFoundException("Term not found"));

        t.setLocked(locked);
        return TermMapper.toResponse(termRepository.save(t));
    }
}
