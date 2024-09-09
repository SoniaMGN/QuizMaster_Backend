package com.quizmaster.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long key;

    private String teacherID;

    private String gradeLevel;

    private String homeroomClass;

    private int yearsOfExperience;

    private String classroomNumber;

    private String contactNumber;

    @JsonIgnore
    @ManyToMany(mappedBy = "teachers")
    private List<Course> courses;

    @OneToMany(mappedBy = "teacher")
    private List<Student> students;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true) // Ensure this matches your schema
    private User user;
}
