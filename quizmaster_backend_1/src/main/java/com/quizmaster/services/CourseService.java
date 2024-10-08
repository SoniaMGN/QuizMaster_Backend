package com.quizmaster.services;

import com.quizmaster.entities.Course;
import com.quizmaster.entities.Student;
import com.quizmaster.entities.Teacher;
import com.quizmaster.entities.User;
import com.quizmaster.models.*;
import com.quizmaster.repositories.CourseRepository;
import com.quizmaster.repositories.StudentRepository;
import com.quizmaster.repositories.TeacherRepository;
import com.quizmaster.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private UsersService usersService;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    public ResponseEntity<String> createCourse(CourseRequestModel requestModel) {

        User currentUser = usersService.currentUser();

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
        }

        if (currentUser.isSuperuser() || "teacher".equals(currentUser.getRole())) {

            Optional<Course> existingCourse = courseRepository.findByCourseName(requestModel.getCourseName());
            if (existingCourse.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Course with the same name already exists.");
            }

            Course newCourse = Course.builder()
                    .courseCode(MyUtils.generateCourseCode(requestModel.getCourseName())) // Generate course code
                    .courseName(requestModel.getCourseName())
                    .description(requestModel.getDescription())
                    .semester(requestModel.getSemester())
                    .capacity(requestModel.getCapacity())
                    .build();

            courseRepository.save(newCourse);

            return ResponseEntity.status(HttpStatus.OK).body("Course created successfully.");
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have permission to create a course.");
    }


    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public Optional<Course> getCourseByCode(String courseCode) {
        return courseRepository.findByCourseCode(courseCode);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }


    public ResponseEntity<String> updateCourse(UpdateCourseRequestModel requestModel) {
        // Get the current user
        User currentUser = usersService.currentUser();

        // Check if the user is authenticated
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
        }

        // Find the existing course by the old course code
        Optional<Course> existingCourseOpt = courseRepository.findByCourseCode(requestModel.getOldCourseCode());

        // Check if the user has permission to update the course
        if (currentUser.isSuperuser() || "teacher".equals(currentUser.getRole())) {

            if (existingCourseOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course does not exist.");
            }

            Course updatedCourse = existingCourseOpt.get();

            // Update the course details
            if (!Objects.equals(updatedCourse.getCourseName(), requestModel.getNewCourseName())) {
                updatedCourse.setCourseCode(MyUtils.generateCourseCode(requestModel.getNewCourseName()));
                updatedCourse.setCourseName(requestModel.getNewCourseName());
            }

            updatedCourse.setDescription(requestModel.getNewDescription());
            updatedCourse.setCapacity(requestModel.getNewCapacity());
            updatedCourse.setSemester(requestModel.getSemester());

            // Save the updated course
            courseRepository.save(updatedCourse);

            return ResponseEntity.status(HttpStatus.OK).body("Course updated successfully.");
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have permission to update the course.");
    }




    public ResponseEntity<String> deleteCourse(String courseCode) {

        User currentUser = usersService.currentUser();

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
        }

        if (currentUser.isSuperuser() || "teacher".equals(currentUser.getRole())) {
            Optional<Course> existingCourse = courseRepository.findByCourseCode(courseCode);

            if (existingCourse.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
            }

            courseRepository.deleteByCourseCode(courseCode);
            return ResponseEntity.status(HttpStatus.OK).body("Course deleted successfully.");
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have permission to delete the course.");
    }


    public ResponseEntity<String> assignTeacherToCourse(AssignCourseRequestModel requestModel) {
        // Get the current user
        User currentUser = usersService.currentUser();

        // Check if the user is authenticated
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
        }

        // Find the course by courseCode
        Optional<Course> courseOpt = courseRepository.findByCourseCode(requestModel.getCourseCode());
        if (courseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }

        Course course = courseOpt.get();

        // If the current user is a superuser, they can assign any teacher to the course
        if (currentUser.isSuperuser()) {
            String teacherId = requestModel.getTeacherId();
            Optional<Teacher> teacherOpt = teacherRepository.findByTeacherID(teacherId);
            if (teacherOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Teacher not found.");
            }
            Teacher teacher = teacherOpt.get();

            // Assign the teacher to the course
            course.getTeachers().add(teacher);
            courseRepository.save(course);

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Teacher " + teacher.getUser().getFirstName() + " " + teacher.getUser().getLastName() + " assigned to the course successfully.");
        }

        // If the current user is a teacher, they can only assign themselves
        if ("teacher".equals(currentUser.getRole())) {
            String currentUserId = String.valueOf(currentUser.getKey());
            String requestTeacherId = requestModel.getTeacherId();

            if (!currentUserId.equals(requestTeacherId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only assign the course to yourself.");
            }

            Optional<Teacher> teacherOpt = teacherRepository.findByTeacherID(currentUserId);
            if (teacherOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Current teacher not found.");
            }
            Teacher teacher = teacherOpt.get();

            // Assign the current teacher to the course
            course.getTeachers().add(teacher);
            courseRepository.save(course);

            return ResponseEntity.status(HttpStatus.OK).body("Course assigned to yourself successfully.");
        }

        // If the user is not authorized to assign a course, return forbidden
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have permission to assign this course.");
    }


    public ResponseEntity<String> enrollStudent(EnrollStudentRequestModel requestModel) {
        // Get the current user
        User currentUser = usersService.currentUser();

        // Check if the user is authenticated
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
        }

        // Find the course by courseId
        Optional<Course> courseOpt = courseRepository.findById(requestModel.getCourseId());
        if (courseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }

        Course course = courseOpt.get();

        // Find the student by studentId
        Optional<Student> studentOpt = studentRepository.findById(requestModel.getStudentId());
        if (studentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found.");
        }

        Student student = studentOpt.get();

        // If the current user is a superuser, they can enroll any student
        if (currentUser.isSuperuser()) {
            // Enroll the student in the course
            if (course.getStudents().contains(student)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Student is already enrolled in this course.");
            }
            course.getStudents().add(student);
            courseRepository.save(course);

            return ResponseEntity.status(HttpStatus.OK).body("Student enrolled in the course successfully.");
        }

        // If the current user is a teacher, they can only enroll students in their own courses
        if ("teacher".equals(currentUser.getRole())) {
            Optional<Teacher> teacherOpt = teacherRepository.findById(currentUser.getKey());
            if (teacherOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Current teacher not found.");
            }
            Teacher teacher = teacherOpt.get();

            // Check if the course belongs to the current teacher
            if (!course.getTeachers().contains(teacher)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to enroll students in this course.");
            }

            // Enroll the student in the course
            if (course.getStudents().contains(student)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Student is already enrolled in this course.");
            }
            course.getStudents().add(student);
            courseRepository.save(course);

            return ResponseEntity.status(HttpStatus.OK).body("Student enrolled in the course successfully.");
        }

        // If the user is not authorized to enroll students, return forbidden
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have permission to enroll students in this course.");
    }


    public ResponseEntity<String> removeStudent(RemoveStudentRequestModel requestModel) {
        // Get the current user
        User currentUser = usersService.currentUser();

        // Check if the user is authenticated
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated.");
        }

        // Find the course by courseId
        Optional<Course> courseOpt = courseRepository.findById(requestModel.getCourseId());
        if (courseOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found.");
        }

        Course course = courseOpt.get();

        // Find the student by studentId
        Optional<Student> studentOpt = studentRepository.findById(requestModel.getStudentId());
        if (studentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student not found.");
        }

        Student student = studentOpt.get();

        // If the current user is a superuser, they can remove any student from any course
        if (currentUser.isSuperuser()) {
            // Remove the student from the course
            if (!course.getStudents().remove(student)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student is not enrolled in this course.");
            }
            courseRepository.save(course);

            return ResponseEntity.status(HttpStatus.OK).body("Student removed from the course successfully.");
        }

        // If the current user is a teacher, they can only remove students from their own courses
        if ("teacher".equals(currentUser.getRole())) {
            Optional<Teacher> teacherOpt = teacherRepository.findById(currentUser.getKey());
            if (teacherOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Current teacher not found.");
            }
            Teacher teacher = teacherOpt.get();

            // Check if the course belongs to the current teacher
            if (!course.getTeachers().contains(teacher)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to remove students from this course.");
            }

            // Remove the student from the course
            if (!course.getStudents().remove(student)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Student is not enrolled in this course.");
            }
            courseRepository.save(course);

            return ResponseEntity.status(HttpStatus.OK).body("Student removed from the course successfully.");
        }

        // If the user is not authorized to remove students, return forbidden
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User does not have permission to remove students from this course.");
    }


    public List<Course> getCoursesByTeacher(String teacherId) {
        // Validate teacherId
        if (teacherId == null) {
            throw new IllegalArgumentException("Teacher ID cannot be null.");
        }

        // Find courses by teacher ID
        return courseRepository.findCoursesByTeacherId(teacherId);
    }
}
