package com.quizmaster.repositories;

import com.quizmaster.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User,Long> {

    @Query("select u from users u where u.email=?1")
    Optional<User> findByEmail(String email);

}
