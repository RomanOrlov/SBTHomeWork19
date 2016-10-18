package ru.sbt.orlov.dao;

import ru.sbt.orlov.entity.Lesson;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LessonDaoImpl implements LessonDao {
    private final Connection connection;

    public LessonDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Lesson> getStudentLessons(int studentId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * "
                    + "FROM LESSONS "
                    + "    LEFT JOIN STUDENTS_VISIT "
                    + "    ON STUDENTS_VISIT.LESSON_ID = LESSONS.ID "
                    + "    AND STUDENTS_VISIT.STUDENT_ID = ? "
                    + "    LEFT JOIN SUBJECTS "
                    + "    ON LESSONS.SUBJECT_ID = SUBJECTS.ID "
                    + "ORDER BY SUBJECTS.NAME ASC");
            preparedStatement.setInt(1,studentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Lesson> lessons = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                int subjectId = resultSet.getInt("SUBJECT_ID");
                LocalDateTime date = resultSet.getTimestamp("DATE").toLocalDateTime();
                Lesson lesson = new Lesson(id, subjectId, date);
                lessons.add(lesson);
            }
            return lessons;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<Lesson> getSubjectLessons(int subjectId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM LESSONS WHERE SUBJECT_ID = ?");
            preparedStatement.setInt(1,subjectId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Lesson> lessons = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                LocalDateTime date = resultSet.getTimestamp("DATE").toLocalDateTime();
                Lesson lesson = new Lesson(id, subjectId, date);
                lessons.add(lesson);
            }
            return lessons;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public Lesson saveLesson(Lesson lesson) {
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO LESSONS (SUBJECT_ID, DATE) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, lesson.getSubject_id());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(lesson.getDate()));
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            int id = generatedKeys.getInt(1);
            connection.commit();
            return new Lesson(id,lesson.getSubject_id(),lesson.getDate());
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
