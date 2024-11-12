package com.onlybuns.onlybuns.Repository;

import com.onlybuns.onlybuns.Model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    Optional<User> findByActivationToken(String token);
}

