package org.example.hibernate.starter.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "students")

public class StudentEntity {
    private Integer id;

    private String name;
    private Integer courseId;
    private String description;
    private String seat;
    public StudentEntity(String name, Integer courseId, String description, String seat) {
        this.name = name;
        this.courseId = courseId;
        this.description = description;
        this.seat = seat;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "student_id")
    public int getId() {
        return id;
    }

    @Column(name = "student_name")
    public String getName() {
        return name;
    }

    @Column(name = "course_id")
    public Integer getCourseId() {
        return courseId;
    }

    public String getDescription() {
        return description;
    }

    public String getSeat() {
        return seat;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", courseId=" + courseId +
                ", description='" + description + '\'' +
                ", seat='" + seat + '\'' +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }
}
