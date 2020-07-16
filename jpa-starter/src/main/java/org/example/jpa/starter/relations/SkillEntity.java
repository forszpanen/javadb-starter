package org.example.jpa.starter.relations;

import org.example.jpa.starter.inheritance.Student;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "skills")
public class SkillEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    public Set<StudentEntity> getStudents() {
        return students;
    }

    public void addStudent(StudentEntity student) {
        this.students.add(student);
        student.getSkills().add(this);
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "students_skills",
            joinColumns = {@JoinColumn(name ="skill_id")},
            inverseJoinColumns = {@JoinColumn(name ="student_id")})
    private Set<StudentEntity> students = new HashSet<>();

    protected SkillEntity() {}

    public SkillEntity(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "SkillEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", students=" + students.stream().map(studentEntity -> studentEntity.getName()).collect(Collectors.joining(",")) +
                '}';
    }
}
