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
public class Student extends User { // Inherits User fields

    @Column(nullable = false)
    private String school;

    private String grade;

    private String dateOfBirth;

    private String address;

    @Column(unique = true)
    private String studentID; // Unique student identifier

    @OneToOne
    @JoinColumn(name = "user_id", unique = true) // Ensure this matches your schema
    private User user;
    // Many-to-Many relationship with Course
    @ManyToMany
    @JoinTable(
            name = "student_courses",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses; // Courses the student is enrolled in

    @Column(nullable = false)
    private String registrationDate;

    private String parentName;

    private String parentContact;

    // Many-to-One relationship with Teacher
    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher; // The student's assigned teacher (nullable for 'Not assigned')

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfCreation;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfUpdate;

    @PrePersist
    protected void onCreate() {
        this.dateOfCreation = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        this.dateOfUpdate = new Date();
    }

}
