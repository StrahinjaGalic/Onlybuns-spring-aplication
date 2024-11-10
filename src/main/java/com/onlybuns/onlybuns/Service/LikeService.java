package com.onlybuns.onlybuns.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlybuns.onlybuns.Model.Like;
import com.onlybuns.onlybuns.Model.Post;
import com.onlybuns.onlybuns.Repository.LikeRepository;

@Service
public class LikeService {
    @Autowired
    public LikeRepository likeRepository;

    public Like createLike(Like like)
    {
        return likeRepository.save(like);
    }
    public List<Like> getAllLikesByPost(Long postId)
    {
        return likeRepository.findByPostId(postId);
    }
    public List<Like> getAllLikesByUser(String username)
    {
        return likeRepository.findByUsername(username);
    }
    public void deleteLike(long id)
    {
        likeRepository.deleteById(id);
    }
    public boolean userHasLikedPost(Long postId,String username)
    {
        return likeRepository.existsByPostIdAndUsername(postId,username);
    }
    public int countLikesByPost(Long postId)
    {
        return (int) likeRepository.countByPostId(postId);
    }
    public Long getLikeIdByPostIdAndUsername(Long postId,String username)
    {
        return likeRepository.getLikeIdByPostIdAndUsername(postId,username);
    }

}
