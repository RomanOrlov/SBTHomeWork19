package ru.sbt.orlov.entity;

/**
 *
 * @author r.orlov
 */
public class Student {
    private final Integer id;
    private final String name;
    private final String surname;

    public Student(Integer id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public Student(String name, String surname) {
        this(null,name,surname);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}
