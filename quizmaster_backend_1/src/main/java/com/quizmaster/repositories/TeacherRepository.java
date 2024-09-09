package com.quizmaster.repositories;

import com.quizmaster.entities.Teacher;
import com.quizmaster.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    // Find a teacher by the associated user entity
    Optional<Teacher> findByUser(User user);

    // Custom query to find a teacher by user email
    @Query("SELECT t FROM Teacher t WHERE t.user.email = ?1")
    Optional<Teacher> findByUserEmail(String email);

    @Query("SELECT t FROM Teacher t WHERE t.teacherID = ?1")
    Optional<Teacher> findByTeacherID(String teacherID);

    // Optionally, you could add more custom queries if needed
}
