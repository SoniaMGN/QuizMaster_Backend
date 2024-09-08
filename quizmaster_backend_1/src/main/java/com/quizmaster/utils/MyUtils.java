package com.quizmaster.utils;

import org.springframework.validation.FieldError;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import java.security.SecureRandom;
import java.util.Random;

public class MyUtils {

    private static final ObjectMapper objectMapper=new ObjectMapper();
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int ID_LENGTH = 8; // Adjust length as needed

    private static final int PASSWORD_LENGTH = 14;
    private static final Random RANDOM = new SecureRandom();
    public static Random random=new Random();

    public static String createErrorMessage(BindingResult result) {
        try {
            List<FieldError> fieldErrors = result.getFieldErrors();

            List<String> errorMessages = new ArrayList<>();
            for (FieldError fError : fieldErrors) {
                Map<String, String> fieldErrorMap = new HashMap<>();
                fieldErrorMap.put(fError.getField(), fError.getDefaultMessage());
                errorMessages.add(objectMapper.writeValueAsString(fieldErrorMap));
            }

            Map<String, List<String>> errorMessageMap = new HashMap<>();
            errorMessageMap.put("fielderrors", errorMessages);


            return objectMapper.writeValueAsString(errorMessageMap);
        } catch (Exception e) {
            // Handle the exception (e.g., log it) or throw a more specific exception
            return ""; // Return an empty string or handle the error as needed
        }
    }

    public static String generateTeacherID() {
        StringBuilder sb = new StringBuilder("TE");
        for (int i = 0; i < ID_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public static String generateStudentID() {
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public static String generateRandomPassword() {
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public static String generateVerificationCode(int charactersCount)
    {
        String code="";
        for(int i=0;i<charactersCount;i++)
        {
            int curr=random.nextInt(10);
            code+=Integer.toString(curr);
        }

        return code;
    }


    public static void sendCode(String email, String verificationCode) {
        System.out.println("sending Email Verification Code for "+email+" is "+verificationCode);
        EmailUtils.sendVerificationCode(email,verificationCode);
    }

    public static String generateCourseCode(String courseName) {
        String initials = courseName.length() >= 3 ? courseName.substring(0, 3).toUpperCase() : courseName.toUpperCase();

        Random random = new Random();
        int randomNumber = 200 + random.nextInt(299);  // Generate a number between 1000 and 9999

        return initials + randomNumber;
    }

}
