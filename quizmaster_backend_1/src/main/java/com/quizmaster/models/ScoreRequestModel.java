package com.quizmaster.models;

import lombok.*;
import javax.validation.constraints.NotNull;

@Data
public class ScoreRequestModel {
    @NotNull(message = "Score cannot be empty")
    private int score;

    @NotNull(message = "QuizID cannot be empty")
    private Long quizID;
}
