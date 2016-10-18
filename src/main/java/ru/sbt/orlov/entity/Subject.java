package ru.sbt.orlov.entity;

/**
 *
 * @author r.orlov
 */
public class Subject {
    private final Integer id;
    private final String name;
    private final String description;

    public Subject(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Subject(String name, String description) {
        this(null,name,description);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
