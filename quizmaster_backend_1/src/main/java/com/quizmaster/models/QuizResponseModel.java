package com.quizmaster.models;

import com.quizmaster.entities.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizResponseModel {

    private Long quizId;
    private String message;
    private List<Question> questions;
}
