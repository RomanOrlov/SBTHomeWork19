package ru.sbt.orlov.dao;

import ru.sbt.orlov.entity.Student;
import ru.sbt.orlov.entity.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StudentDaoImpl implements StudentDao {
    private final Connection connection;

    public StudentDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Student findById(int studentId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM STUDENTS WHERE ID = ?");
            preparedStatement.setInt(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("NAME");
                String surname = resultSet.getString("SURNAME");
                return new Student(studentId, name, surname);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public long getStudentSubjectVisits(int studentId, int subjectId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(ID) AS STUDENT_SUBJECT_VISITS "
                    + "FROM STUDENTS_VISITS WHERE STUDENT_ID = ? "
                    + "AND LESSON_ID IN (SELECT ID FROM LESSONS WHERE SUBJECT_ID = ?)");
            preparedStatement.setInt(1, studentId);
            preparedStatement.setInt(2, subjectId);
            ResultSet resultSet = preparedStatement.executeQuery();
            long visits = resultSet.getLong("STUDENT_SUBJECT_VISITS");
            return visits;
        } catch (SQLException ex) {
            Logger.getLogger(StudentDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    /**
     *
     * @param studentId Уникальный id студента
     * @return Список предметов, на которые ходит студент
     */
    @Override
    public List<Subject> getStudentSubjects(int studentId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM SUBJECTS WHERE SUBJECTS.ID IN ("
                    + "    SELECT LESSONS.SUBJECT_ID "
                    + "     FROM LESSONS "
                    + "     LEFT JOIN STUDENTS_VISIT "
                    + "     ON LESSONS.ID = STUDENTS_VISIT.LESSON_ID "
                    + "     AND STUDENTS_VISIT.STUDENT_ID = ?"
                    + ") ");
            preparedStatement.setInt(1, studentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Subject> subjects = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                String description = resultSet.getString("DESCRIPTION");
                Subject subject = new Subject(id, name, description);
                subjects.add(subject);
            }
            return subjects;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public Student saveStudent(Student student) {
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO STUDENTS (NAME, SURNAME) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, student.getName());
            preparedStatement.setString(2, student.getSurname());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            int id = generatedKeys.getInt(1);
            connection.commit();
            return new Student(id,student.getName(),student.getSurname());
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                throw new RuntimeException(e);
            }
            throw new RuntimeException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<Student> getStudentsList() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM STUDENTS");
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Student> students = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("NAME");
                String surname = resultSet.getString("SURNAME");
                Student student = new Student(id, name, surname);
                students.add(student);
            }
            return students;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }
    
    
}
