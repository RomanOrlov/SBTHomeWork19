package ru.sbt.orlov.dao;

import ru.sbt.orlov.entity.StudentVisit;

import java.util.List;

public interface StudentVisitsDao {
    List<StudentVisit> getStudentVisits(int studentId);

    StudentVisit saveStudentVisit(StudentVisit studentVisit);
}
