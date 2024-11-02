package com.onlybuns.onlybuns.Service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlybuns.onlybuns.Model.Image;
import com.onlybuns.onlybuns.Model.Post;
import com.onlybuns.onlybuns.Repository.ImageRepository;
import com.onlybuns.onlybuns.Repository.PostRepository;

import io.jsonwebtoken.io.IOException;

@Service
public class PostService {
    
    @Autowired
    public PostRepository postRepository;

    @Autowired
    public ImageService imageService;


    public Post createPost(Post post){
        Post savedPost = postRepository.save(post);
        return savedPost;
    }

   
}
    
