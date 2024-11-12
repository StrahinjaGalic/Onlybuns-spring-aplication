package com.onlybuns.onlybuns.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlybuns.onlybuns.Model.Follow;
import com.onlybuns.onlybuns.Service.FollowService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/follows")
public class FollowController {
    @Autowired
    FollowService followService;

    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestBody Follow follow) {
        try 
        {
            if (!follow.getUsername().isEmpty() && !follow.getUsername().isEmpty()) 
            {
                followService.create(follow);
                return new ResponseEntity<>("Follow created.", HttpStatus.CREATED);
            } else 
            {
                return new ResponseEntity<>("Usernames must not be empty.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) 
        {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{username}/following")
    public ResponseEntity<List<Follow>> getAllFollowing(@PathVariable String username) 
    {
        List<Follow> following = followService.getAllFollowing(username);
        return ResponseEntity.ok(following);
    }
    @GetMapping("/{username}/followers")
    public ResponseEntity<List<Follow>> getAllFollowers(@PathVariable String username)
    {
        List<Follow> followers = followService.getAllFollowers(username);
        return ResponseEntity.ok(followers);
    }
}
