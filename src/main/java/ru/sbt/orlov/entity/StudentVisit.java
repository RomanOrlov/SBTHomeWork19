package ru.sbt.orlov.entity;

/**
 *
 * @author r.orlov
 */
public class StudentVisit {
    private final Integer id;
    private final Integer student_id;
    private final Integer lesson_id;

    public StudentVisit(Integer id, Integer student_id, Integer lesson_id) {
        this.id = id;
        this.student_id = student_id;
        this.lesson_id = lesson_id;
    }

    public StudentVisit(Integer student_id, Integer lesson_id) {
        this(null,student_id,lesson_id);
    }
    public Integer getId() {
        return id;
    }

    public Integer getStudent_id() {
        return student_id;
    }

    public Integer getLesson_id() {
        return lesson_id;
    }
}
