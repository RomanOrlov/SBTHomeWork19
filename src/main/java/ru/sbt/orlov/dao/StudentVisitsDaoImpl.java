package ru.sbt.orlov.dao;

import ru.sbt.orlov.entity.StudentVisit;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentVisitsDaoImpl implements StudentVisitsDao {
    private final Connection connection;

    public StudentVisitsDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<StudentVisit> getStudentVisits(int studentId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID,LESSON_ID FROM STUDENTS_VISIT WHERE STUDENT_ID = ?");
            preparedStatement.setInt(1,studentId);
            ResultSet executeQuery = preparedStatement.executeQuery();
            List<StudentVisit> studentVisits = new ArrayList<>();
            while (executeQuery.next()) {
                Integer id = executeQuery.getInt("ID");
                Integer lesson_id = executeQuery.getInt("LESSON_ID");
                studentVisits.add(new StudentVisit(id,studentId,lesson_id));
            }
            return studentVisits;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return Collections.EMPTY_LIST;
        }
    }

    @Override
    public StudentVisit saveStudentVisit(StudentVisit studentVisit) {
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO STUDENTS_VISIT (STUDENT_ID, LESSON_ID) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1,studentVisit.getStudent_id());
            preparedStatement.setInt(2, studentVisit.getLesson_id());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            int id = generatedKeys.getInt(1);
            connection.commit();
            return new StudentVisit(id,studentVisit.getStudent_id(),studentVisit.getLesson_id());
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
}
