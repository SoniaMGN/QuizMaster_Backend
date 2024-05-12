package com.quizmaster.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "question")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "key")
    private Long key;

    private String question;

    private String answer;

    private String option_1;

    private String option_2;

    private String option_3;

    private String option_4;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "quiz_key")
    private Quiz quiz;
}
