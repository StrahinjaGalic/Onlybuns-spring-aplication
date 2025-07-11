package com.onlybuns.onlybuns.Repository;
import com.onlybuns.onlybuns.Model.Post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post,Long> {
    Post findById(long id);
    List<Post> findByUsernameAndDeletedFalse(String username);
    List<Post> findByDeletedFalse();
    @Query("SELECT p FROM Post p WHERE p.deleted = false ORDER BY p.advertised DESC, p.creationTime DESC")
    List<Post> findAllPostsSorted();
}
