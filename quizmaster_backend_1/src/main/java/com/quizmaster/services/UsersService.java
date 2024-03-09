package com.quizmaster.services;


import com.quizmaster.entities.AuthorizationTokens;
import com.quizmaster.entities.User;
import com.quizmaster.models.*;
import com.quizmaster.repositories.AuthorizationTokensRepository;
import com.quizmaster.repositories.UsersRepository;
import com.quizmaster.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateRequestCustomizer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorizationTokensRepository authorizationTokensRepository;

    @Autowired
    private RestTemplate restTemplate;

    public String registerUser(String email,String firstName,String lastName,String password)
    {
        User user=User.builder().email(email).firstName(firstName).lastName(lastName).password(password).build();

        usersRepository.save(user);

        return "User saved Successfully. User : "+user;
    }

    public List<User> listUsers() {
        return usersRepository.findAll();
    }

    public ResponseEntity<RegisterResponseModel> registerUser(RegisterRequestModel registerRequestModel) {

        Optional<User> userOptional=usersRepository.findByEmail(registerRequestModel.getEmail());
        if(userOptional.isPresent())
        {
            RegisterResponseModel response= RegisterResponseModel.builder()
                    .userId(null)
                    .message("Email already Registered")
                    .build();

            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        User newUser=User.builder()
                .email(registerRequestModel.getEmail()).
                firstName(registerRequestModel.getFirstName())
                .lastName(registerRequestModel.getLastName())
                .password(passwordEncoder.encode(registerRequestModel.getPassword()))
                .build();

        usersRepository.saveAndFlush(newUser);

        RegisterResponseModel response= RegisterResponseModel.builder()
                .userId(newUser.getKey())
                .message("Registration Successful")
                .build();

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<User> getCurrentUser() {
        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> optionalUser=usersRepository.findByEmail(email);
        if(optionalUser.isPresent()) {
            User currUser = optionalUser.get();
            return ResponseEntity.ok(currUser);
        }
        else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

    }

    private User currentUser()
    {
        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> optionalUser=usersRepository.findByEmail(email);
        if(optionalUser.isPresent()) {
            User currUser = optionalUser.get();
            return currUser;
        }
        else{
            return null;
        }
    }



}
