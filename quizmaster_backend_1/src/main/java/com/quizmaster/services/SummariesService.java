package com.quizmaster.services;

import com.quizmaster.entities.Summary;
import com.quizmaster.entities.User;
import com.quizmaster.models.*;
import com.quizmaster.repositories.SummariesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;


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
                    .content(summaryRequestModel.getContent())
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

    public ResponseEntity<String> getNotes(String title)
    {
        User myUser = userService.currentUser();

        if (myUser == null)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {

            Summary summary = summariesRepository.findByTitleAndUserKey(myUser.getKey(), title);

            return ResponseEntity.ok(summary.getContent());
        }


        //return ResponseEntity.ok(myUser.getSummaries());//summariesRepository.findByUserID(currentUser().getKey()));
    }
}
