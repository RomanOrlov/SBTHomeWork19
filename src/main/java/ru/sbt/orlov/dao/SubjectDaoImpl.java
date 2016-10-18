package ru.sbt.orlov.dao;

import ru.sbt.orlov.entity.Subject;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubjectDaoImpl implements SubjectDao {
    private final Connection connection;

    public SubjectDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Subject> getSubjects() {
        try {
            ResultSet resultSet = connection.prepareStatement("SELECT * FROM SUBJECTS").executeQuery();
            List<Subject> subjects = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                Subject subject = new Subject(id,name,description);
                subjects.add(subject);
            }
            return subjects;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public Subject saveSubject(Subject subject) {
        try {
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO SUBJECTS (NAME, DESCRIPTION) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,subject.getName());
            preparedStatement.setString(2, subject.getDescription());
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            generatedKeys.next();
            int id = generatedKeys.getInt(1);
            connection.commit();
            return new Subject(id,subject.getName(),subject.getDescription());
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
