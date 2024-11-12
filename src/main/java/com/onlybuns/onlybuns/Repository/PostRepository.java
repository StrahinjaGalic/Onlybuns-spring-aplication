package com.onlybuns.onlybuns.Repository;
import com.onlybuns.onlybuns.Model.Post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
    Post findById(long id);
    List<Post> findByUsernameAndDeletedFalse(String username);
    List<Post> findByDeletedFalse();
}
