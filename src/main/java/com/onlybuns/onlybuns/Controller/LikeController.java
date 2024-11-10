package com.onlybuns.onlybuns.Controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlybuns.onlybuns.Dto.LikeDto;
import com.onlybuns.onlybuns.Model.Like;
import com.onlybuns.onlybuns.Service.LikeService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/likes")
public class LikeController {
    
    @Autowired
    public LikeService likeService;

    @PostMapping("/create")
    public ResponseEntity<String> createLike(@RequestBody Like like) 
    {
        try
        {
            if(like.getPost() != null & like.getUsername() != null)
            {
                likeService.createLike(like);
                return new ResponseEntity<>("Like created.",HttpStatus.CREATED);
            }
            else
            {
                return new ResponseEntity<>("Like post or username not found.",HttpStatus.BAD_REQUEST);
            }
        } 
        catch (DataIntegrityViolationException e) 
        {
            return new ResponseEntity<>("User has already liked this post.", HttpStatus.BAD_REQUEST);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteLike(@PathVariable Long id)
    {
        try
        {
            likeService.deleteLike(id);
            return new ResponseEntity<>("Like deleted.",HttpStatus.OK); 
        }
        catch (Exception e) 
        {
            return new ResponseEntity<>("Failed to remove like. " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/user/{username}")
    public ResponseEntity<List<LikeDto>> getLikesByUsername(@PathVariable String username) 
    {
        List<Like> likes = likeService.getAllLikesByUser(username);
        List<LikeDto> likeDtos = likes.stream().map(like -> new LikeDto(like.getId(),like.getPost().getId(),like.getUsername())).collect(Collectors.toList());
        return ResponseEntity.ok(likeDtos);
    }
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<LikeDto>> getLikesByPost(@PathVariable Long postId) 
    {
        List<Like> likes = likeService.getAllLikesByPost(postId);
        List<LikeDto> likeDtos = likes.stream().map(like -> new LikeDto(like.getId(),like.getPost().getId(),like.getUsername())).collect(Collectors.toList());
        return ResponseEntity.ok(likeDtos);
    }
    
    

}
