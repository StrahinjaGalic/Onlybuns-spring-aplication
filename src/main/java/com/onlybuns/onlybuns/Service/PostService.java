package com.onlybuns.onlybuns.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlybuns.onlybuns.Model.Post;
import com.onlybuns.onlybuns.Repository.PostRepository;

@Service
public class PostService {
    
    @Autowired
    public PostRepository postRepository;


    public Post createPost(Post post){
        Post savedPost = postRepository.save(post);
        return savedPost;
    }

    
    
}
