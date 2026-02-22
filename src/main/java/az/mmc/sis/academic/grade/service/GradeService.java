package az.mmc.sis.academic.grade.service;

import az.mmc.sis.academic.assignment.model.TeachingAssignment;
import az.mmc.sis.academic.assignment.repository.TeachingAssignmentRepository;
import az.mmc.sis.academic.grade.dto.*;
import az.mmc.sis.academic.grade.mapper.GradeMapper;
import az.mmc.sis.academic.grade.model.Grade;
import az.mmc.sis.academic.grade.repository.GradeRepository;
import az.mmc.sis.academic.term.repository.TermRepository;
import az.mmc.sis.common.exception.BadRequestException;
import az.mmc.sis.common.exception.ForbiddenException;
import az.mmc.sis.common.exception.NotFoundException;
import az.mmc.sis.user.model.Role;
import az.mmc.sis.user.model.User;
import az.mmc.sis.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GradeService {

    private final GradeRepository gradeRepository;
    private final UserRepository userRepository;
    private final TeachingAssignmentRepository assignmentRepository;
    private final TermRepository termRepository;

    @Transactional
    public GradeResponse upsertGrade(GradeRequest request, String teacherEmail) {

        User teacher = userRepository.findByEmail(teacherEmail)
                .orElseThrow(() -> new NotFoundException("Teacher not found"));

        TeachingAssignment assignment = assignmentRepository.findById(request.assignmentId())
                .orElseThrow(() -> new NotFoundException("Assignment not found"));

        if (!assignment.getTeacher().getId().equals(teacher.getId())) {
            throw new ForbiddenException("You can only grade your own assignments");
        }

        // TERM LOCK CHECK
        termRepository.findByActiveTrue().ifPresent(t -> {
            if (t.isLocked()) {
                throw new ForbiddenException("Active term is locked. Grades cannot be modified.");
            }
        });

        User student = userRepository.findById(request.studentId())
                .orElseThrow(() -> new NotFoundException("Student not found"));

        if (student.getRole() != Role.STUDENT) {
            throw new BadRequestException("User is not a student");
        }

        double finalScore = (request.quiz() * 0.2) + (request.midterm() * 0.3) + (request.finalExam() * 0.5);
        String letter = calculateLetter(finalScore);

        Grade grade = gradeRepository
                .findByStudentIdAndTeachingAssignmentId(request.studentId(), request.assignmentId())
                .orElseGet(() -> Grade.builder()
                        .student(student)
                        .teachingAssignment(assignment)
                        .build());

        grade.setQuiz(request.quiz());
        grade.setMidterm(request.midterm());
        grade.setFinalExam(request.finalExam());
        grade.setFinalScore(finalScore);
        grade.setLetterGrade(letter);

        Grade saved = gradeRepository.save(grade);
        return GradeMapper.toTeacherResponse(saved);
    }

    public List<StudentGradeResponse> getMyGrades(String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new NotFoundException("Student not found"));

        return gradeRepository.findByStudentId(student.getId())
                .stream()
                .map(GradeMapper::toStudentGrade)
                .toList();
    }

    public StudentGpaResponse getMyGpa(String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new NotFoundException("Student not found"));

        List<Grade> grades = gradeRepository.findByStudentId(student.getId());

        if (grades.isEmpty()) {
            return StudentGpaResponse.builder().gpa(0.0).totalCourses(0).build();
        }

        double totalWeightedPoints = grades.stream()
                .mapToDouble(g -> convertLetterToPoint(g.getLetterGrade()) * g.getTeachingAssignment().getSubject().getCredit())
                .sum();

        int totalCredits = grades.stream()
                .mapToInt(g -> g.getTeachingAssignment().getSubject().getCredit())
                .sum();

        double gpa = totalCredits == 0 ? 0.0 : totalWeightedPoints / totalCredits;

        return StudentGpaResponse.builder()
                .gpa(Math.round(gpa * 100.0) / 100.0)
                .totalCourses(grades.size())
                .build();
    }

    public TranscriptResponse getMyTranscript(String studentEmail) {
        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new NotFoundException("Student not found"));

        List<Grade> grades = gradeRepository.findByStudentId(student.getId());

        List<TranscriptCourseResponse> courses = grades.stream()
                .map(GradeMapper::toTranscriptCourse)
                .toList();

        double totalWeightedPoints = grades.stream()
                .mapToDouble(g -> convertLetterToPoint(g.getLetterGrade()) * g.getTeachingAssignment().getSubject().getCredit())
                .sum();

        int totalCredits = grades.stream()
                .mapToInt(g -> g.getTeachingAssignment().getSubject().getCredit())
                .sum();

        double gpa = totalCredits == 0 ? 0.0 : Math.round((totalWeightedPoints / totalCredits) * 100.0) / 100.0;

        return TranscriptResponse.builder()
                .studentId(student.getId())
                .studentName(student.getFirstName() + " " + student.getLastName())
                .groupName(student.getGroup() != null ? student.getGroup().getName() : null)
                .gpa(gpa)
                .totalCourses(grades.size())
                .courses(courses)
                .build();
    }

    private String calculateLetter(double score) {
        if (score >= 90) return "A";
        if (score >= 80) return "B";
        if (score >= 70) return "C";
        if (score >= 60) return "D";
        return "F";
    }

    private double convertLetterToPoint(String letter) {
        return switch (letter) {
            case "A" -> 4.0;
            case "B" -> 3.0;
            case "C" -> 2.0;
            case "D" -> 1.0;
            default -> 0.0;
        };
    }

    public AcademicSummaryResponse getAcademicSummary(String studentEmail) {

        User student = userRepository.findByEmail(studentEmail)
                .orElseThrow(() -> new NotFoundException("Student not found"));

        List<Grade> grades = gradeRepository.findByStudentId(student.getId());

        if (grades.isEmpty()) {
            return AcademicSummaryResponse.builder()
                    .gpa(0.0)
                    .totalCourses(0)
                    .totalCreditsAttempted(0)
                    .totalCreditsPassed(0)
                    .failedCourses(0)
                    .build();
        }

        int totalCreditsAttempted = 0;
        int totalCreditsPassed = 0;
        int failedCourses = 0;
        double totalWeightedPoints = 0;

        for (Grade g : grades) {
            int credit = g.getTeachingAssignment().getSubject().getCredit();
            totalCreditsAttempted += credit;

            double point = convertLetterToPoint(g.getLetterGrade());
            totalWeightedPoints += point * credit;

            if ("F".equals(g.getLetterGrade())) {
                failedCourses++;
            } else {
                totalCreditsPassed += credit;
            }
        }

        double gpa = totalCreditsAttempted == 0
                ? 0.0
                : Math.round((totalWeightedPoints / totalCreditsAttempted) * 100.0) / 100.0;

        return AcademicSummaryResponse.builder()
                .gpa(gpa)
                .totalCourses(grades.size())
                .totalCreditsAttempted(totalCreditsAttempted)
                .totalCreditsPassed(totalCreditsPassed)
                .failedCourses(failedCourses)
                .build();
    }
}
