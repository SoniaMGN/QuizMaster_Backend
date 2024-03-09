package com.quizmaster.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationTokens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long key;

    private String value;

    private Date created;

    private Boolean hasLoggedOut;

    private Date logoutTime;

    @JsonIgnore
    private Long verifyDate;

    @JsonIgnore
    private String verificationCode;

    public Boolean isVerified()
    {
        return verifyDate!=null&&verifyDate>0L;
    }
}
