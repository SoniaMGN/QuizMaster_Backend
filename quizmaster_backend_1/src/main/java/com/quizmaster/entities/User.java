package com.quizmaster.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@Data
@Builder
@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long key;

    private String email;

    private String firstName;

    private String lastName;

    @JsonIgnore
    private String password;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Summary> summaries;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Quiz> quizzes;

    private String role;

    private String school;

    private String status;

    @Column(name = "is_superuser", nullable = false)
    private boolean isSuperuser = false;


    public User(String email, String name, String lastname, String password) {
        setEmail(email);
        setFirstName(name);
        setLastName(lastname);
        setPassword(password);
    }
}
