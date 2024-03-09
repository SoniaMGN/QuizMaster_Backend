package com.quizmaster.controllers;

import com.quizmaster.Auth.RegisteredUserDetailService;
import com.quizmaster.configurations.WebSecurityConfig;
import com.quizmaster.entities.AuthorizationTokens;
import com.quizmaster.entities.User;
import com.quizmaster.models.*;
import com.quizmaster.utils.MyUtils;
import com.quizmaster.repositories.AuthorizationTokensRepository;
import com.quizmaster.services.UsersService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RegisteredUserDetailService registeredUserDetailService;

    @Autowired
    private AuthorizationTokensRepository authorizationTokensRepository;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseModel> register(@RequestBody @Valid RegisterRequestModel registerRequestModel, BindingResult result)
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
            return usersService.registerUser(registerRequestModel);
    }

}
