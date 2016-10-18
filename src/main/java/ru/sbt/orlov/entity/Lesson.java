package ru.sbt.orlov.entity;

import java.time.LocalDateTime;

/**
 *
 * @author r.orlov
 */
public class Lesson {
    private final Integer id;
    private final int subject_id;
    private final LocalDateTime date;

    public Lesson(Integer id, int subject_id, LocalDateTime date) {
        this.id = id;
        this.subject_id = subject_id;
        this.date = date;
    }

    public Lesson(int subject_id, LocalDateTime date) {
        this(null,subject_id,date);
    }

    public int getId() {
        return id;
    }

    public Integer getSubject_id() {
        return subject_id;
    }

    public LocalDateTime getDate() {
        return date;
    }
}
