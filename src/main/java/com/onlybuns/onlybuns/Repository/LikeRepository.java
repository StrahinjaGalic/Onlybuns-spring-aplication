package com.onlybuns.onlybuns.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.onlybuns.onlybuns.Model.Like;

import java.util.List;


public interface LikeRepository extends JpaRepository<Like,Long> {
    Like findById(long id);
    List<Like> findByUsername(String username);
    List<Like> findByPostId(Long id);
    boolean existsByPostIdAndUsername(Long postId,String username);
    long countByPostId(Long postId);
    
    @Query("SELECT l.id FROM Like l WHERE l.post.id = :postId AND l.username = :username")
    Long getLikeIdByPostIdAndUsername(@Param("postId") Long postId, @Param("username") String username);
}
