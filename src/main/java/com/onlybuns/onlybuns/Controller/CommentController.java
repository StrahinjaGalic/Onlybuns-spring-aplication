package com.onlybuns.onlybuns.Controller;

import java.util.List;
import java.util.stream.Collectors;


import org.apache.catalina.filters.RateLimitFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlybuns.onlybuns.Dto.CommentDto;
import com.onlybuns.onlybuns.Model.Comment;
import com.onlybuns.onlybuns.Model.User;
import com.onlybuns.onlybuns.Service.CommentService;
import com.onlybuns.onlybuns.Service.RateLimiterService;
import com.onlybuns.onlybuns.Service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/comments")
public class CommentController {
    @Autowired
    public CommentService commentService;

    @Autowired
    public UserService userService;

    private final RateLimiterService rateLimiterService;

    @Autowired
    public CommentController(RateLimiterService rateLimiterService) {
        this.rateLimiterService = rateLimiterService;

    }
    @PostMapping("/create")
    public ResponseEntity<String> createComment(@RequestBody Comment comment) {
        try
        {
            User user = userService.findUserByUsername(comment.getUsername()).get();
            if(!comment.getComment().isEmpty() && comment.getPost() != null && !comment.getUsername().isEmpty() && rateLimiterService.isRequestAllowed(user.getId()))
            {
                commentService.createComment(comment);
                return new ResponseEntity<>("Comment created.", HttpStatus.CREATED);
            }
            else
            {
                return new ResponseEntity<>("Comment must not be empty.", HttpStatus.BAD_REQUEST);
            }
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id)
    {
        try
        {
            commentService.deleteComment(id);
            return new ResponseEntity<>("Comment deleted.",HttpStatus.OK);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>("Failed to delete comment.",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/user/{username}")
    public ResponseEntity<List<CommentDto>> getCommentsByUsername(@PathVariable String username) 
    {
        List<Comment> comments = commentService.getAllCommentsByUser(username);
        List<CommentDto> commentDtos = comments.stream().map(comment -> new CommentDto(comment.getId(),comment.getPost().getId(),comment.getUsername(),comment.getComment(), comment.getCommentDate(), comment.isEdited(), comment.isDeleted())).collect(Collectors.toList());
        return ResponseEntity.ok(commentDtos);
    }
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDto>> getCommentsByPost(@PathVariable Long postId) 
    {
        List<Comment> comments = commentService.getAllCommentsByPost(postId);
        List<CommentDto> commentDtos = comments.stream().map(comment -> new CommentDto(comment.getId(),comment.getPost().getId(),comment.getUsername(),comment.getComment(), comment.getCommentDate(), comment.isEdited(), comment.isDeleted())).collect(Collectors.toList());
        return ResponseEntity.ok(commentDtos);
    }
    
}