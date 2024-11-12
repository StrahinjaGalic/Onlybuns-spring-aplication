package com.onlybuns.onlybuns.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.onlybuns.onlybuns.Model.Comment;
import java.util.List;


public interface CommentRepository extends JpaRepository<Comment,Long> {
    Comment findById(long id);
    List<Comment> findByUsername(String username);
    List<Comment> findByPostId(Long postId);
}
