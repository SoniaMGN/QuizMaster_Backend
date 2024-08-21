package com.quizmaster.repositories;
import com.quizmaster.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {

    @Query("select u from students u where u.email=?1")
    Optional<Student> findByEmail(String email);

    @Query("select u.key from students u where u.email=?1")
    Long findKeyByEmail(String email);
}
