package com.onlybuns.onlybuns.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlybuns.onlybuns.Dto.FollowResponseDto;
import com.onlybuns.onlybuns.Model.Follow;
import com.onlybuns.onlybuns.Repository.FollowRepository;

import jakarta.transaction.Transactional;

@Service
public class FollowService {
    @Autowired
    FollowRepository followRepository;

    @Transactional
    public Follow create(Follow follow) 
    {
        if(follow.getUsername().isEmpty() && follow.getFollowingUsername().isEmpty())
        {
            throw new IllegalArgumentException("Usernames must not be empty.");
        }
        if (followRepository.existsByUsernameAndFollowingUsername(follow.getUsername(), follow.getFollowingUsername())) 
        {
            throw new IllegalArgumentException("User already follows this person.");
        }
        if(follow.getUsername().equals(follow.getFollowingUsername()))
        {
            throw new IllegalArgumentException("User cannot follow himself dummy.");
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
    public void deleteById(long id)
    {
        followRepository.deleteById(id);
    }
    public FollowResponseDto getFollowStatus(String username, String followingUsername)
    {
        Follow follow = followRepository.findByUsernameAndFollowingUsername(username,followingUsername);

        if(follow != null)
        {
            return new FollowResponseDto(true,follow.getId());
        }
        else
        {
            return new FollowResponseDto(false,null);
        }

    }
}
