package com.quizmaster.utils;

import org.springframework.validation.FieldError;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class MyUtils {

    private static final ObjectMapper objectMapper=new ObjectMapper();

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
}
