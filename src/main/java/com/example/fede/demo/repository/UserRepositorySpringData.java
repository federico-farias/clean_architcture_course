package com.example.fede.demo.repository;

import com.example.fede.demo.model.User;
import com.example.fede.demo.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepositorySpringData extends JpaRepository<UserEntity, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    @Query("SELECT u FROM UserEntity u WHERE u.enabled = true")
    List<User> findAllActiveUsers();
    
    @Query("SELECT u FROM UserEntity u WHERE u.firstName LIKE %:name% OR u.lastName LIKE %:name%")
    List<User> findByName(@Param("name") String name);
}