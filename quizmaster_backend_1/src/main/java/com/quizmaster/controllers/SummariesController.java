
package com.quizmaster.controllers;

import com.quizmaster.Auth.RegisteredUserDetailService;
import com.quizmaster.configurations.WebSecurityConfig;
import com.quizmaster.entities.AuthorizationTokens;
import com.quizmaster.entities.Summary;
import com.quizmaster.entities.User;
import com.quizmaster.models.*;
import com.quizmaster.repositories.SummariesRepository;
import com.quizmaster.services.SummariesService;
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
@RequestMapping("/api/v1/summary")
public class SummariesController {

    @Autowired
    private SummariesService summariesService;

    @Autowired
    private UsersService usersService;

    @Autowired
    private SummariesRepository summariesRepository;


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
            return summariesService.saveSummary(summaryRequestModel);
    }

    @GetMapping("/mySummaries")
    public ResponseEntity<List<Summary>> mySummaries(){ return summariesService.listSummaries(); }

    @GetMapping("/myContent")
    public ResponseEntity<String> myContent(@RequestParam("title") String title){
        return summariesService.getNotes(title);
    }

    @DeleteMapping("/deleteSummary/{summaryKey}")
    public ResponseEntity<String> deleteSummary(@PathVariable("summaryKey") Long summaryKey) {
        User myUser = usersService.currentUser();

        if (myUser != null) {
            Optional<Summary> mysummary = summariesRepository.findById(summaryKey);
            if (mysummary.isPresent()) {
                Summary summary = mysummary.get();
                if (summary.getUser().getKey().equals(myUser.getKey())) {
                    summariesRepository.delete(summary);

                    return ResponseEntity.ok("Summary deleted");

                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("summary does not belong to user");
                }


            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("summary not found");

            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("user not authorised");
    }

}
