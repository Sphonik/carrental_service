package com.carrental.repository;

import com.carrental.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Method to find a user by their username
    User findByUsername(String username);
    // Method to find a user by their username
    
}
