package ru.sbt.orlov.dao;

import ru.sbt.orlov.entity.Subject;

import java.util.List;

public interface SubjectDao {
    List<Subject> getSubjects();

    Subject saveSubject(Subject subject);
}
