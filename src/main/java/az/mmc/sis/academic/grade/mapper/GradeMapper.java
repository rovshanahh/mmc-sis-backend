package az.mmc.sis.academic.grade.mapper;

import az.mmc.sis.academic.grade.dto.GradeResponse;
import az.mmc.sis.academic.grade.dto.StudentGradeResponse;
import az.mmc.sis.academic.grade.dto.TranscriptCourseResponse;
import az.mmc.sis.academic.grade.model.Grade;

public final class GradeMapper {

    private GradeMapper() {}

    public static GradeResponse toTeacherResponse(Grade g) {
        return GradeResponse.builder()
                .id(g.getId())
                .studentId(g.getStudent().getId())
                .studentName(g.getStudent().getFirstName() + " " + g.getStudent().getLastName())
                .assignmentId(g.getTeachingAssignment().getId())
                .subjectName(g.getTeachingAssignment().getSubject().getName())
                .quiz(g.getQuiz())
                .midterm(g.getMidterm())
                .finalExam(g.getFinalExam())
                .finalScore(g.getFinalScore())
                .letterGrade(g.getLetterGrade())
                .build();
    }

    public static StudentGradeResponse toStudentGrade(Grade g) {
        return StudentGradeResponse.builder()
                .gradeId(g.getId())
                .assignmentId(g.getTeachingAssignment().getId())
                .subjectName(g.getTeachingAssignment().getSubject().getName())
                .quiz(g.getQuiz())
                .midterm(g.getMidterm())
                .finalExam(g.getFinalExam())
                .finalScore(g.getFinalScore())
                .letterGrade(g.getLetterGrade())
                .build();
    }

    public static TranscriptCourseResponse toTranscriptCourse(Grade g) {
        return TranscriptCourseResponse.builder()
                .assignmentId(g.getTeachingAssignment().getId())
                .subjectName(g.getTeachingAssignment().getSubject().getName())
                .finalScore(g.getFinalScore() == null ? 0.0 : g.getFinalScore())
                .letterGrade(g.getLetterGrade())
                .build();
    }
}
