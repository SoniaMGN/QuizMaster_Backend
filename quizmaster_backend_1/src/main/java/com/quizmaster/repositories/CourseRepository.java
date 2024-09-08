package com.quizmaster.repositories;

import com.quizmaster.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // Custom JPQL query to find all courses by a teacher's ID
    @Query("SELECT c FROM course c WHERE c.teacher.key = :teacherId")
    List<Course> findCoursesByTeacherId(@Param("teacherId") String teacherId);

    // Custom query to find a course by courseCode
    Optional<Course> findByCourseCode(Long courseCode);


}
