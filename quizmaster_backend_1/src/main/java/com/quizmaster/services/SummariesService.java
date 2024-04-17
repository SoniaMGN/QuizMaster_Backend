package com.quizmaster.services;

import com.quizmaster.entities.AuthorizationTokens;
import com.quizmaster.entities.Summary;
import com.quizmaster.entities.User;
import com.quizmaster.models.*;
import com.quizmaster.repositories.AuthorizationTokensRepository;
import com.quizmaster.repositories.SummariesRepository;
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
import org.springframework.dao.DataIntegrityViolationException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SummariesService {

    @Autowired
    private SummariesRepository summariesRepository;

    @Autowired
    private UsersService userService;
    public ResponseEntity<SummaryResponseModel> saveSummary(SummaryRequestModel summaryRequestModel) {
        User myUser = userService.currentUser();
        if (myUser == null)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else
        {
            Summary newSummary = Summary.builder()
                    .date(summaryRequestModel.getDate())
                    .title(summaryRequestModel.getTitle())
                    .summary(summaryRequestModel.getSummary())
                    .user(myUser)
                    .build();

            summariesRepository.save(newSummary);

            SummaryResponseModel response = SummaryResponseModel.builder()
                    .summaryId(newSummary.getKey())
                    .message("Summary was saved successfully")
                    .build();

            return ResponseEntity.ok(response);
        }


    }

    public ResponseEntity<List<Summary>> listSummaries()
    {
        User myUser = userService.currentUser();

        return ResponseEntity.ok(myUser.getSummaries());//summariesRepository.findByUserID(currentUser().getKey()));
    }
}
