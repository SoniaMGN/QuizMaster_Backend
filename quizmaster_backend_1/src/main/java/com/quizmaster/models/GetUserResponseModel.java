package com.quizmaster.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.quizmaster.entities.User;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUserResponseModel {

    private User user;

}
