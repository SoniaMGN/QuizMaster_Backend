package com.quizmaster.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.util.List;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Teacher extends User {

    private String teacherID;

    private String school;

    @JsonIgnore
    @ManyToMany(mappedBy = "teachers")
    private List<Course> courses;

    @OneToMany(mappedBy = "teacher")
    private List<Student> students;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true) // Ensure this matches your schema
    private User user;
}
