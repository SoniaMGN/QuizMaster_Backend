package com.quizmaster.controllers;

import com.quizmaster.entities.Course;
import com.quizmaster.entities.User;
import com.quizmaster.models.*;
import com.quizmaster.services.CourseService;
import com.quizmaster.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private UsersService usersService;

    // Create a new course
    @PostMapping("/create")
    public ResponseEntity<String> createCourse(@RequestBody CourseRequestModel requestModel) {
        return courseService.createCourse(requestModel);
    }

    // Get a course by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Optional<Course>> getCourseById(@PathVariable Long id) {
        Optional<Course> course = courseService.getCourseById(id);
        return course.isPresent() ? ResponseEntity.ok(course) : ResponseEntity.notFound().build();
    }

    // Get all courses
    @GetMapping("/all")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    // Update a course
    @PutMapping("/update")
    public ResponseEntity<String> updateCourse(@RequestBody UpdateCourseRequestModel requestModel) {
        return courseService.updateCourse(requestModel);
    }

    // Delete a course by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable String id) {
        return courseService.deleteCourse(id);
    }

    // Assign a teacher to a course
    @PostMapping("/assign-teacher")
    public ResponseEntity<String> assignTeacherToCourse(@RequestBody AssignCourseRequestModel requestModel) {
        return courseService.assignTeacherToCourse(requestModel);
    }

    // Enroll a student in a course
    @PostMapping("/enroll-student")
    public ResponseEntity<String> enrollStudent(@RequestBody EnrollStudentRequestModel requestModel) {
        return courseService.enrollStudent(requestModel);
    }

    // Remove a student from a course
    @PostMapping("/remove-student")
    public ResponseEntity<String> removeStudent(@RequestBody RemoveStudentRequestModel requestModel) {
        return courseService.removeStudent(requestModel);
    }

    // Get courses by teacher ID (only for superusers and teachers)
    @GetMapping("/by-teacher")
    public ResponseEntity<?> getCoursesByTeacher(@RequestParam String teacherId) {
        User currentUser = usersService.currentUser();
        if (currentUser == null) {
            return ResponseEntity.status(401).body("User is not authenticated.");
        }
        if (!currentUser.isSuperuser() && !"teacher".equals(currentUser.getRole())) {
            return ResponseEntity.status(403).body("User does not have permission to access this information.");
        }
        List<Course> courses = courseService.getCoursesByTeacher(teacherId);
        return ResponseEntity.ok(courses);
    }
}
