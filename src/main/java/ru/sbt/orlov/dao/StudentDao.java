package ru.sbt.orlov.dao;

import ru.sbt.orlov.entity.Student;
import ru.sbt.orlov.entity.Subject;

import java.util.List;

public interface StudentDao {
    Student findById(int studentId);
    
    long getStudentSubjectVisits(int studentId,int subjectId);

    List<Subject> getStudentSubjects(int studentId);
    
    List<Student> getStudentsList();

    Student saveStudent(Student student);
}
