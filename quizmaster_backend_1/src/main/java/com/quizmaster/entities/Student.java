package com.quizmaster.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Student  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long key;

    private String grade;

    private String dateOfBirth;

    private String address;

    @Column(unique = true)
    private String studentID;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDate;

    private String parentName;

    private String parentContact;


    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfCreation;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfUpdate;

    @PrePersist
    protected void onCreate() {
        this.dateOfCreation = new Date();
        this.registrationDate = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dateOfUpdate = new Date();
    }

}
