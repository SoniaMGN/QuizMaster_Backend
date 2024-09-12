package com.quizmaster.controllers;

import com.quizmaster.models.*;
import com.quizmaster.services.TeacherService;
import com.quizmaster.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register-teacher")
    public ResponseEntity<RegisterResponseModel> register(@RequestBody RegisterTeacherRequestModel registerRequestModel, BindingResult result) {
        if (result.hasErrors()) {
            String msg = MyUtils.createErrorMessage(result);

            RegisterResponseModel registerResponseModel = RegisterResponseModel.builder()
                    .userId(null)
                    .message(msg)
                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(registerResponseModel);
        } else {
            return teacherService.registerTeacher(registerRequestModel);
        }
    }

    @PostMapping("/admin-register-teacher")
    public ResponseEntity<RegisterResponseModel> registerTeacher(@RequestBody @Valid TeacherRegisterRequestModel registerRequestModel, BindingResult result)
    {
        if(result.hasErrors())
        {

            String msg= MyUtils.createErrorMessage(result);



            RegisterResponseModel registerResponseModel=RegisterResponseModel.builder()
                    .userId(null)
                    .message(msg)

                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(registerResponseModel);

        }
        else
            return teacherService.registerTeacherByAdmin(registerRequestModel);
    }
}
