package ru.sbt.orlov.dao;

import ru.sbt.orlov.entity.Lesson;

import java.util.List;

public interface LessonDao {
    List<Lesson> getStudentLessons(int studentId);

    List<Lesson> getSubjectLessons(int subjectId);

    Lesson saveLesson(Lesson lesson);
}
