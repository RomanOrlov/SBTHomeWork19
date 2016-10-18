package ru.sbt.orlov;

import ru.sbt.orlov.dao.SubjectDaoImpl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import javafx.util.converter.LocalDateTimeStringConverter;
import ru.sbt.orlov.dao.LessonDao;
import ru.sbt.orlov.dao.LessonDaoImpl;
import ru.sbt.orlov.dao.StudentDao;
import ru.sbt.orlov.dao.StudentDaoImpl;
import ru.sbt.orlov.dao.StudentVisitsDao;
import ru.sbt.orlov.dao.StudentVisitsDaoImpl;
import ru.sbt.orlov.dao.SubjectDao;
import ru.sbt.orlov.entity.Lesson;
import ru.sbt.orlov.entity.Student;
import ru.sbt.orlov.entity.StudentVisit;
import ru.sbt.orlov.entity.Subject;

/**
 *
 * @author r.orlov
 */
public class Main {
    public static void main(String[] args) throws SQLException {
        try (Connection connection = getConnection("jdbc:h2:~/test", "sa", "")) {
            try {
                createTables(connection);
                randomDataGenerator(connection);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static void randomDataGenerator(Connection connection) {
        SubjectDao subjectDao = new SubjectDaoImpl(connection);
        LessonDao lessonDao = new LessonDaoImpl(connection);
        StudentDao studentDao = new StudentDaoImpl(connection);
        StudentVisitsDao studentVisitsDao = new StudentVisitsDaoImpl(connection);
        subjectDao.saveSubject(new Subject("Математический анализ", "Математично"));
        subjectDao.saveSubject(new Subject("Экономика", ""));
        subjectDao.saveSubject(new Subject("Английский", "На него кто то ходит"));
        subjectDao.saveSubject(new Subject("Физика", "Ведет профессор ля ля ля"));
        subjectDao.saveSubject(new Subject("Квантовая механика", "Изучаем котиков Шредингера"));
        subjectDao.saveSubject(new Subject("Химия", "Ребят, смотрите че это я получил"));
        subjectDao.saveSubject(new Subject("Аналитическая геометрия", "Вводим инварианты"));
        subjectDao.saveSubject(new Subject("МРТ", ""));
        subjectDao.saveSubject(new Subject("Рад. излуч. в биол. мед.", "Никому не нужна"));
        String[] names = {"Артём", "Андрей", "Алексей", "Роман", "Петр", "Вадим",
            "Сергей", "Татьяна", "Анастасия", "Наталья", "Виктория", "Екатерина", "Федор", "Кирил"};
        String[] surname = {"Семашко", "Петренко", "Стульченко", "Гожана", "Седова",
            "Желудь", "Щершавый", "Бетон", "Кальченко", "Нигматзянов", "Хорошо", "Железобетон",
            "Закрома", "Колмычко", "Вжиха", "Супренко", "Гирляда", "Ничоси", "Фильченко",
            "Хромов", "Ахтыжёж", "Ломатель", "Боржоми"};
        subjectDao.getSubjects().forEach(subject -> {
            System.out.println(subject);
            //There is 16 lessons for every subject
            LocalDateTime now = LocalDateTime.of(2016, Month.SEPTEMBER, 1, (int)Math.random()*12, 0);
            for (int i=0;i< 16;i++) {
                Lesson lesson = new Lesson(subject.getId(), now.plusWeeks(i));
                lessonDao.saveLesson(lesson);
            }
            List<Lesson> lessons = lessonDao.getSubjectLessons(subject.getId());
            // On every subject is somewhere 8 students
            for (int i=0;i<(int) (Math.random() * 16);i++) {
                Student student = new Student(names[(int) (Math.random() * names.length)] , surname[(int) (Math.random() * surname.length)]);
                student = studentDao.saveStudent(student);
                // Every student going on the subject with 90% chance
                for (Lesson lesson : lessons) {
                    if (Math.random() <= 0.9d) {
                        StudentVisit studentVisit = new StudentVisit(student.getId(),lesson.getId());
                        studentVisitsDao.saveStudentVisit(studentVisit);
                    }
                }
            }
        });
    }

    private static void createTables(Connection connection) throws SQLException {
        connection.createStatement().executeUpdate("CREATE TABLE STUDENTS" +
         "(" +
         "id INT NOT NULL AUTO_INCREMENT," +
         "name VARCHAR(50)," +
         "surname VARCHAR(50)," +
         "PRIMARY KEY(id)" +
         ");");

        connection.createStatement().executeUpdate("CREATE TABLE SUBJECTS" +
        "(" +
        "id INT NOT NULL AUTO_INCREMENT," +
        "name VARCHAR(50) NOT NULL," +
        "description VARCHAR(255)," +
        "PRIMARY KEY(id)" +
        ");");

        connection.createStatement().executeUpdate("CREATE TABLE LESSONS" +
        "(" +
        "id INT NOT NULL AUTO_INCREMENT," +
        "subject_id INT NOT NULL," +
        "date SMALLDATETIME NOT NULL," +
        "PRIMARY KEY(id)," +
        "FOREIGN KEY (subject_id) REFERENCES SUBJECTS(id)" +
        ");");

        connection.createStatement().executeUpdate("CREATE TABLE STUDENTS_VISIT" +
        "(" +
        "id INT NOT NULL AUTO_INCREMENT," +
        "student_id INT NOT NULL," +
        "lesson_id INT NOT NULL," +
        "PRIMARY KEY(id)," +
        "FOREIGN KEY (student_id) REFERENCES STUDENTS(id)," +
        "FOREIGN KEY (lesson_id) REFERENCES LESSONS(id)" +
        ");");
    }
}
