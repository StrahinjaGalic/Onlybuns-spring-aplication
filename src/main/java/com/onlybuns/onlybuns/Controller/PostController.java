package com.onlybuns.onlybuns.Controller;

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
            postService.createPost(post);
            return new ResponseEntity<>("Post created.",HttpStatus.OK);
        } catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

}
