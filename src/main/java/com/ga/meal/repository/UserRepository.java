package com.ga.meal.repository;

import com.ga.meal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by email.
     *
     * @param email the user's email
     * @return user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Checks if an email already exists.
     *
     * @param email the user's email
     * @return true if email exists
     */
    boolean existsByEmail(String email);
}