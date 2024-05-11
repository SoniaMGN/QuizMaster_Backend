package com.quizmaster.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequestModel {

    private String question;

    private String answer;

    private String option_1;

    private String option_2;

    private String option_3;

    private String option_4;

}
