package com.onlybuns.onlybuns.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlybuns.onlybuns.Model.Post;
import com.onlybuns.onlybuns.Repository.PostRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    
    @Autowired
    public PostRepository postRepository;

    // If you are going to use ImageService, you might want to keep this
    @Autowired
    private ImageService imageService;

    public Post createPost(Post post) {
        return postRepository.save(post); // Saving post and returning the saved instance
    }

    @Transactional
    public List<Post> getAllPosts() {
        return postRepository.findByDeletedFalse(); // Retrieve all posts
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id); // Retrieve a post by its ID
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post not found with Id " + id));
        post.setDeleted(true);
        postRepository.save(post);
    }

    @Transactional
    public List<Post> getByUsername(String username) {
        return postRepository.findByUsernameAndDeletedFalse(username); // Retrieve all posts by a specific user
    }
}
