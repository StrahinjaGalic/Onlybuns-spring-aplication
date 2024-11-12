package com.onlybuns.onlybuns.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.onlybuns.onlybuns.Model.Post;
import com.onlybuns.onlybuns.Service.PostService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/posts")
public class PostController {
    
    @Autowired
    private PostService postService;
    
    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestBody Post post) {
        try {
            // Check if the post has an image and the image data is not empty
            if (post.getImage() != null && post.getImage().getData() != null && !post.getImage().getData().isEmpty()) {
                postService.createPost(post);
                return new ResponseEntity<>("Post created.", HttpStatus.CREATED); // Use 201 for resource creation
            } else {
                return new ResponseEntity<>("Image data is required.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // Use 500 for server errors
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getPost(@PathVariable long id) {
        Optional<Post> post = postService.getPostById(id); // Call the service method
        return post.map(ResponseEntity::ok) // Return 200 with the post if found
                   .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Return 404 if not found
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts(); // Retrieve all posts
        return ResponseEntity.ok(posts); // Return 200 with the list of posts
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<Post>> getPostsByUsername(@PathVariable String username) {
        List<Post> posts = postService.getByUsername(username); // Retrieve posts by username
        return ResponseEntity.ok(posts); // Return 200 with the list of posts
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id)
    {
        try
        {
            postService.deletePost(id);
            return new ResponseEntity<>("Post deleted.",HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>("Failed to delete post.",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editPost(@PathVariable Long id,@Valid @RequestBody Post updatedPost) 
    {
        try
        {
            Optional<Post> updated = postService.updatePost(id, updatedPost);

            if(updated.isPresent())
            {
                return new ResponseEntity<>("Post edited.",HttpStatus.OK);
            }
            return new ResponseEntity<>("The post you are trying to edit doesn't exist.",HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>("Failed to edit post.",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
