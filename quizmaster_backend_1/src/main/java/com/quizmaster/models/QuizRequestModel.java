package com.quizmaster.models;
import com.quizmaster.entities.Question;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data

public class QuizRequestModel {

    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @NotEmpty(message = "Question list cannot be empty")
    private List<Question> questionList;


}
