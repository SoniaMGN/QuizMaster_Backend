package com.quizmaster.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "teachers")
@DiscriminatorValue("teacher")
@NoArgsConstructor
@AllArgsConstructor
public class Teacher extends User {

    private String teacherID;
    private String school;

    @JsonIgnore
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Student> students;

    @JsonIgnore
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Course> courses;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
