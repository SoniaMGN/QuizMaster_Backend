package com.quizmaster.repositories;

import com.quizmaster.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // Custom JPQL query to find all courses by a teacher's ID
    @Query("SELECT c FROM Course c JOIN c.teachers t WHERE t.key = :teacherId")
    List<Course> findCoursesByTeacherId(@Param("teacherId") String teacherId);

    // Custom query to find a course by courseCode
    @Query("SELECT c FROM Course c WHERE c.courseCode = :courseCode")
    Optional<Course> findByCourseCode(@Param("courseCode") String courseCode);

    // Custom query to find a course by courseName
    @Query("SELECT c FROM Course c WHERE c.courseName = :courseName")
    Optional<Course> findByCourseName(@Param("courseName") String courseName);

    // Custom query to delete a course by courseCode
    @Modifying
    @Transactional
    @Query("DELETE FROM Course c WHERE c.courseCode = :courseCode")
    void deleteByCourseCode(@Param("courseCode") String courseCode);
}
