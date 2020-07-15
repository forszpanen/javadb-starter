package org.example.hibernate.starter.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "courses")
public class CourseEntity {
    private Integer id;
    private String name;
    private String place;
    private Date startDate;
    private Date endDate;

    private CourseEntity() {
    }

    public CourseEntity(String name, String place, Date startDate, Date endDate) {

        this.name = name;
        this.place = place;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "course_id")
    public Integer getId() {
        return id;
    }

    @Column(name = "course_name")
    public String getName() {
        return name;
    }

    public String getPlace() {
        return place;
    }

    @Column(name = "start_date")
    public Date getStartDate() {
        return startDate;
    }

    @Column(name = "end_date")
    public Date getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "CourseEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", place='" + place + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
