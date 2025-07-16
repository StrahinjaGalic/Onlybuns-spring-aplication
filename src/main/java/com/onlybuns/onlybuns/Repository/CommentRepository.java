package com.onlybuns.onlybuns.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.onlybuns.onlybuns.Model.Comment;

import java.util.Date;
import java.util.List;


public interface CommentRepository extends JpaRepository<Comment,Long> {
    Comment findById(long id);
    List<Comment> findByUsername(String username);
    List<Comment> findByPostId(Long postId);

    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId ORDER BY c.commentDate DESC")
    List<Comment> findByPostIdNewToOld(Long postId);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.username = :username AND c.commentDate >= :oneHourAgo")
    int countCommentsInLastHour(@Param("username") String username, @Param("oneHourAgo") Date oneHourAgo);
}
