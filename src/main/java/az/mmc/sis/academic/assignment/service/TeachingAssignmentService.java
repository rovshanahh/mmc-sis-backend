package az.mmc.sis.academic.assignment.service;

import az.mmc.sis.academic.assignment.dto.TeachingAssignmentRequest;
import az.mmc.sis.academic.assignment.dto.TeachingAssignmentResponse;
import az.mmc.sis.academic.assignment.mapper.TeachingAssignmentMapper;
import az.mmc.sis.academic.assignment.model.TeachingAssignment;
import az.mmc.sis.academic.assignment.repository.TeachingAssignmentRepository;
import az.mmc.sis.academic.attendance.dto.LessonStudentResponse;
import az.mmc.sis.academic.group.model.StudentGroup;
import az.mmc.sis.academic.group.repository.StudentGroupRepository;
import az.mmc.sis.academic.subject.model.Subject;
import az.mmc.sis.academic.subject.repository.SubjectRepository;
import az.mmc.sis.common.exception.BadRequestException;
import az.mmc.sis.common.exception.NotFoundException;
import az.mmc.sis.user.model.Role;
import az.mmc.sis.user.model.User;
import az.mmc.sis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeachingAssignmentService {

    private final TeachingAssignmentRepository teachingAssignmentRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;
    private final StudentGroupRepository studentGroupRepository;

    @Transactional
    public TeachingAssignmentResponse assign(TeachingAssignmentRequest request) {

        Long teacherId = request.getTeacherId();
        Long subjectId = request.getSubjectId();
        Long groupId = request.getGroupId();

        User teacher = userRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundException("Teacher not found"));

        if (teacher.getRole() != Role.TEACHER) {
            throw new BadRequestException("User is not a teacher");
        }

        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new NotFoundException("Subject not found"));

        StudentGroup group = studentGroupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not found"));

        TeachingAssignment assignment = teachingAssignmentRepository
                .findByTeacherIdAndSubjectIdAndGroupId(teacherId, subjectId, groupId)
                .orElseGet(() -> teachingAssignmentRepository.save(
                        TeachingAssignment.builder()
                                .teacher(teacher)
                                .subject(subject)
                                .group(group)
                                .build()
                ));

        return TeachingAssignmentMapper.toResponse(assignment);
    }

    public List<TeachingAssignmentResponse> getAll(Long teacherId, Long subjectId, Long groupId) {
        List<TeachingAssignment> list;

        if (teacherId != null) {
            list = teachingAssignmentRepository.findAllByTeacherId(teacherId);
        } else if (subjectId != null) {
            list = teachingAssignmentRepository.findAllBySubjectId(subjectId);
        } else if (groupId != null) {
            list = teachingAssignmentRepository.findAllByGroupId(groupId);
        } else {
            list = teachingAssignmentRepository.findAll();
        }

        return list.stream().map(TeachingAssignmentMapper::toResponse).toList();
    }

    public TeachingAssignmentResponse getById(Long id) {
        TeachingAssignment a = teachingAssignmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Teaching assignment not found"));

        return TeachingAssignmentMapper.toResponse(a);
    }

    public List<TeachingAssignmentResponse> getMyAssignments() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User teacher = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Teacher not found"));

        if (teacher.getRole() != Role.TEACHER) {
            throw new BadRequestException("User is not a teacher");
        }

        return teachingAssignmentRepository.findAllByTeacherId(teacher.getId())
                .stream()
                .map(TeachingAssignmentMapper::toResponse)
                .toList();
    }

    @Transactional
    public void delete(Long id) {
        TeachingAssignment assignment = teachingAssignmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Assignment not found"));

        teachingAssignmentRepository.delete(assignment);
    }

    public List<TeachingAssignmentResponse> getMyAssignments(String teacherEmail) {

        var teacher = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new NotFoundException("Teacher not found"));
    
        return teachingAssignmentRepository
                .findByTeacherId(teacher.getId())
                .stream()
                .map(TeachingAssignmentMapper::toResponse)
                .toList();
    }

    public List<LessonStudentResponse> getStudentsForAssignment(Long assignmentId, String teacherEmail) {

        User teacher = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new NotFoundException("Teacher not found"));

        TeachingAssignment assignment = teachingAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new NotFoundException("Teaching assignment not found"));

        if (!assignment.getTeacher().getId().equals(teacher.getId())) {
            throw new BadRequestException("You can only access your own assignments");
        }

        Long groupId = assignment.getGroup().getId();

        return userRepository.findByGroupId(groupId).stream()
                .filter(u -> u.getRole() == Role.STUDENT)
                .map(u -> LessonStudentResponse.builder()
                        .studentId(u.getId())
                        .studentName(u.getFirstName() + " " + u.getLastName())
                        .build())
                .toList();
    }
}
