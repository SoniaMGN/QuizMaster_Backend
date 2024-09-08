package com.quizmaster.services;

import com.quizmaster.entities.Course;
import com.quizmaster.entities.Student;
import com.quizmaster.entities.Teacher;
import com.quizmaster.repositories.CourseRepository;
import com.quizmaster.repositories.TeacherRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

    public Optional<Course> getCourseByCode(Long courseCode) {
        return courseRepository.findByCourseCode(courseCode);
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course updateCourse(Long id, Course updatedCourse) {
        Optional<Course> existingCourse = courseRepository.findById(id);
        if (existingCourse.isPresent()) {
            Course course = existingCourse.get();
            course.setCourseName(updatedCourse.getCourseName());
            course.setCourseCode(updatedCourse.getCourseCode());
            course.setDescription(updatedCourse.getDescription());
            course.setCreditHours(updatedCourse.getCreditHours());
            course.setSemester(updatedCourse.getSemester());
            course.setTeachers(updatedCourse.getTeachers());
            return courseRepository.save(course);
        } else {
            throw new ResourceNotFoundException("Course not found with ID: " + id);
        }
    }

    public void deleteCourse(Long id) {
        courseRepository.deleteById(id);
    }

    public Course assignTeacherToCourse(Long courseId, List<Teacher> teacher) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            course.setTeachers(teacher);
            return courseRepository.save(course);
        } else {
            throw new ResourceNotFoundException("Course not found with ID: " + courseId);
        }
    }

    public Course enrollStudents(Long courseId, List<Student> students) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            course.getStudents().addAll(students);
            return courseRepository.save(course);
        } else {
            throw new ResourceNotFoundException("Course not found with ID: " + courseId);
        }
    }

    public Course removeStudents(Long courseId, List<Student> students) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isPresent()) {
            Course course = courseOpt.get();
            course.getStudents().removeAll(students);
            return courseRepository.save(course);
        } else {
            throw new ResourceNotFoundException("Course not found with ID: " + courseId);
        }
    }

    public List<Course> getCoursesByTeacher(String teacherId) {
        return courseRepository.findCoursesByTeacherId(teacherId);
    }
}
