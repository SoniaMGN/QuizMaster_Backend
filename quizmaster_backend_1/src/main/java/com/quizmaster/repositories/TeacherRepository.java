package com.quizmaster.repositories;
import com.quizmaster.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher,Long> {

    @Query("select u from teachers u where u.email=?1")
    Optional<Teacher> findByEmail(String email);

    @Query("select u.key from teachers u where u.email=?1")
    Long findKeyByEmail(String email);
}
