package com.tutorial.jwt.repository;

import com.tutorial.jwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
   @Query("SELECT U FROM User U JOIN FETCH U.authorities A " +
           "WHERE U.email = :email")
   Optional<User> findByUserWithAuthority(String email);
}
