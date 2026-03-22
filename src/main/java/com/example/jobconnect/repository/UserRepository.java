package com.example.jobconnect.repository;

import com.example.jobconnect.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Spring magically turns this method name into: 
    // SELECT * FROM users WHERE email = ?
    Optional<User> findByEmail(String email);

    // Useful to check if an email is already registered before creating a new account
    Boolean existsByEmail(String email);
}
