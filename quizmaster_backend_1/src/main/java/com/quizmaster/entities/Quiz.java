package com.quizmaster.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import java.util.List;
import javax.persistence.*;


@Data
@Builder
@JsonIgnoreProperties("questions")
@Table(name = "quiz")
@Entity(name = "quiz")
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "key")
    private Long key;

    private String title;



    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<Question> questions;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_key")
    private User user;

}
