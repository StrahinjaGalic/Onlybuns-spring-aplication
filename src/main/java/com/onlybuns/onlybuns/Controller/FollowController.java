package com.onlybuns.onlybuns.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.onlybuns.onlybuns.Dto.FollowResponseDto;
import com.onlybuns.onlybuns.Model.Follow;
import com.onlybuns.onlybuns.Service.FollowService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/follows")
public class FollowController {
    @Autowired
    FollowService followService;

    @PostMapping("/create")
    public ResponseEntity<FollowResponseDto> createPost(@RequestBody Follow follow) {
        
        Follow followCreated = followService.create(follow);
        if(followCreated != null)
        {
            FollowResponseDto followResponseDto = new FollowResponseDto(true, followCreated.getId());
            return ResponseEntity.ok(followResponseDto);
        }
        else
        {
            FollowResponseDto followResponseDto = new FollowResponseDto(false, null);
            return ResponseEntity.ok(followResponseDto);
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
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteFollowing(@PathVariable long id)
    {
        try
        {
            followService.deleteById(id);
            return new ResponseEntity<>("Follow deleted.",HttpStatus.ACCEPTED);
        }
        catch (Exception e)
        {
            return new ResponseEntity<>("Failed to delete follow.",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/check")
    public ResponseEntity<FollowResponseDto> checkIfFollowing(@RequestParam String username,@RequestParam String followingUsername) 
    {
        FollowResponseDto response = followService.getFollowStatus(username, followingUsername);
        return ResponseEntity.ok(response);
    }
    
}
