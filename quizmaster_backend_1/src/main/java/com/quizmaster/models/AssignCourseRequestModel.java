package com.quizmaster.models;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
public class AssignCourseRequestModel {
        @NotNull(message = "Course ID cannot be null")
        private Long courseId;

        @NotNull(message = "Teacher ID cannot be null")
        private Long teacherId;
}
