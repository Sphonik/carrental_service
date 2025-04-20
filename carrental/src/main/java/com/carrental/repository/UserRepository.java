package com.carrental.repository;

import com.carrental.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Method to find a user by their username
    Optional<User> findByUsername(String username);

    
}
