package com.quizmaster.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import javax.validation.constraints.NotEmpty;


import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SummaryRequestModel {
    @NotEmpty(message = "Date cannot be empty")
    private String date;

    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @NotEmpty(message = "Summary cannot be empty")
    private String summary;

    @NotEmpty(message = "Content cannot be empty")
    private String content;
}
