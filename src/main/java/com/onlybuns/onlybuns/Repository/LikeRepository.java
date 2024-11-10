package com.onlybuns.onlybuns.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlybuns.onlybuns.Model.Like;

import java.util.List;


public interface LikeRepository extends JpaRepository<Like,Long> {
    Like findById(long id);
    List<Like> findByUsername(String username);
    List<Like> findByPostId(Long id);
}
