package com.onlybuns.onlybuns.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlybuns.onlybuns.Model.Follow;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findByUsername(String username);
    List<Follow> findByFollowingUsername(String token);
    long countByUsername(String username);
    long countByFollowingUsername(String followingUsername);
    boolean existsByUsernameAndFollowingUsername(String username, String followingUsername);
}
