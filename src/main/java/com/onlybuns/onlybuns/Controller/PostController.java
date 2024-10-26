package com.onlybuns.onlybuns.Controller;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.onlybuns.onlybuns.Model.Post;
import com.onlybuns.onlybuns.Service.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    
    @Autowired
    private PostService postService;
    
    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestBody Post post) {
        try {
            if (post.getImage() != null && post.getImage().getData() != null && !post.getImage().getData().isEmpty()) {
                postService.createPost(post);
                return new ResponseEntity<>("Post created.", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Image data is required.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable long id){
        return postService.postRepository.findById(id);
    }

}
