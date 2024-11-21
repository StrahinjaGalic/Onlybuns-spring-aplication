package com.onlybuns.onlybuns.Service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlybuns.onlybuns.Model.Location;
import com.onlybuns.onlybuns.Model.Post;
import com.onlybuns.onlybuns.Repository.PostRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;


import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    
    @Autowired
    public PostRepository postRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private LocationService locationService;

    public Post createPost(Post post) {
       Location location = locationService.createLocation(post.getLocation());

        
       post.setLocation(location);

        return postRepository.save(post);
    }

    @Transactional
    public List<Post> getAllPosts() {
        List<Post> posts =  postRepository.findByDeletedFalse(); // Retrieve all posts
        posts.forEach(post -> Hibernate.initialize(post.getLocation()));
        return posts;
    }

    
    public Optional<Post> getPostById(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
 
        if (post.isPresent()) {

            Location location = locationService.getLocationForPost(postId);
            post.get().setLocation(location); // Set the cached location in the Post object
 
            return post;
       }
 
        return Optional.empty();
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

    
    public Optional<Post> updatePost(Long postId,Post updatedPost) {
        Optional<Post> existingPost = postRepository.findById(postId);
        if(existingPost.isPresent())
        {
            Post post = existingPost.get();
            post.setDescription(updatedPost.getDescription());
            post.setLocation(updatedPost.getLocation());
            post.setImage(updatedPost.getImage());
            post.setEdited(true);
            return Optional.of(postRepository.save(post));
        }
        return Optional.empty();
    }
}
