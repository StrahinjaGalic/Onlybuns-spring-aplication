package com.onlybuns.onlybuns.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlybuns.onlybuns.Model.Post;
import com.onlybuns.onlybuns.Repository.PostRepository;

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

    public List<Post> getAllPosts() {
        return postRepository.findAll(); // Retrieve all posts
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id); // Retrieve a post by its ID
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id); // Delete a post by its ID
    }

    public List<Post> getByUsername(String username) {
        return postRepository.findByUsername(username); // Retrieve all posts by a specific user
    }
}
