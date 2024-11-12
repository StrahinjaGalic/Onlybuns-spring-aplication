package com.onlybuns.onlybuns.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlybuns.onlybuns.Model.Follow;
import com.onlybuns.onlybuns.Repository.FollowRepository;

@Service
public class FollowService {
    @Autowired
    FollowRepository followRepository;

    public Follow create(Follow follow) 
    {
        if (followRepository.existsByUsernameAndFollowingUsername(follow.getUsername(), follow.getFollowingUsername())) 
        {
            throw new IllegalArgumentException("User already follows this person.");
        }
        return followRepository.save(follow);
    }
    public List<Follow> getAllFollowing(String username)
    {
        return followRepository.findByUsername(username);
    }
    public List<Follow> getAllFollowers(String username)
    {
        return followRepository.findByFollowingUsername(username);
    }
    public int countFollowing(String username)
    {
        return (int) followRepository.countByUsername(username);
    }
    public int countFollowers(String username)
    {
        return (int) followRepository.countByFollowingUsername(username);
    }
}
