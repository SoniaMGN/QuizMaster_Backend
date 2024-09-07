package com.quizmaster.repositories;

import com.quizmaster.entities.Student;
import com.quizmaster.entities.Teacher;
import com.quizmaster.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // Find a teacher by the associated user entity
    Optional<Student> findByUser(User user);

    // Custom query to find a teacher by user email
    @Query("SELECT s FROM Student s WHERE s.user.email = ?1")
    Optional<Student> findByUserEmail(String email);

    // Optionally, you could add more custom queries if needed
}
