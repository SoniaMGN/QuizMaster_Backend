package com.quizmaster.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.Entity;
import javax.persistence.*;


@Data
@Builder
@Table(name = "scores")
@Entity(name = "scores")
@NoArgsConstructor
@AllArgsConstructor

public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "key")
    private Long key;

    private int score;

    @ManyToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;


}
