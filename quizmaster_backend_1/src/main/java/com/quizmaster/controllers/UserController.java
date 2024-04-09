package com.quizmaster.controllers;

import com.quizmaster.Auth.RegisteredUserDetailService;
import com.quizmaster.configurations.WebSecurityConfig;
import com.quizmaster.entities.AuthorizationTokens;
import com.quizmaster.entities.Summary;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponseModel> login(@RequestBody @Valid LoginRequestModel loginRequestModel)
    {
        //spring security handles login for us
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestModel.getEmail(),loginRequestModel.getPassword())
        );

        UserDetails userDetails = registeredUserDetailService.loadUserByUsername(loginRequestModel.getEmail());

        final String tokens = Jwts.builder().setClaims(new HashMap<>()).setSubject(userDetails.getUsername()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(2)))
                .signWith(SignatureAlgorithm.HS256, WebSecurityConfig.secretKey).compact();


        String authorization="Bearer "+tokens;

        AuthorizationTokens authorizationTokens=AuthorizationTokens.builder()
                .created(new java.util.Date())
                .hasLoggedOut(false)
                .value(authorization)
                .build();

        authorizationTokensRepository.saveAndFlush(authorizationTokens);

        //sendVerificationCode(authorizationTokens,userDetails.getUsername());

        LoginResponseModel loginResponseModel=LoginResponseModel.builder()
                .user(userDetails.getUsername())
                .message("Login Successful.")
                .Authorization(authorization)
                .build();

        return ResponseEntity.ok(loginResponseModel);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logOut(HttpServletRequest request)
    {
        Optional<AuthorizationTokens> optionalAuthorizationTokens=authorizationTokensRepository.findByValue(request.getHeader("Authorization"));
        if(optionalAuthorizationTokens.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Authoriation Not Found");

        AuthorizationTokens authorizationTokens=optionalAuthorizationTokens.get();
        if(authorizationTokens.getHasLoggedOut())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authoriation already Logged Out");

        //perform the logout action
        authorizationTokens.setHasLoggedOut(true);
        authorizationTokens.setLogoutTime(new java.util.Date());

        authorizationTokensRepository.save(authorizationTokens);

        return ResponseEntity.ok("Logged out successfully");

    }

    @PostMapping("/updateUser")
    public ResponseEntity<String> updateUser(@RequestBody @Valid UpdateUserRequestModel updateUserRequestModel,BindingResult result)
    {
        if(result.hasErrors())
        {

            String msg= MyUtils.createErrorMessage(result);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);

        }
        else
            return usersService.updateUser(updateUserRequestModel);
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordRequestModel changePasswordRequestModel,BindingResult result)
    {
        if(result.hasErrors())
        {

            String msg= MyUtils.createErrorMessage(result);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);

        }
        else
            return usersService.changePassword(changePasswordRequestModel);
    }

    @GetMapping("/getCurrentUser")
    public ResponseEntity<User> getUser(){
        User currentUser = usersService.currentUser();
        if (currentUser == null){

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {
            return ResponseEntity.ok(currentUser);
        }

    }
    @PostMapping("/saveSummary")
    public ResponseEntity<SummaryResponseModel> saveSummary(@RequestBody @Valid SummaryRequestModel summaryRequestModel, BindingResult result)
    {
        if(result.hasErrors())
        {

            String msg= MyUtils.createErrorMessage(result);



            SummaryResponseModel summaryResponseModel=SummaryResponseModel.builder()
                    .summaryId(null)
                    .message(msg)

                    .build();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(summaryResponseModel);

        }
        else
            return usersService.saveSummary(summaryRequestModel);
    }

    @GetMapping("/mySummaries")
    public ResponseEntity<List<Summary>> mySummaries(){ return usersService.listSummaries(); }

}


