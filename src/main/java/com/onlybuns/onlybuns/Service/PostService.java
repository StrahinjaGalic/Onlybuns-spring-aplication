package com.onlybuns.onlybuns.Service;

import org.hibernate.Hibernate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.onlybuns.onlybuns.Messaging.RabbitMQConfig;
import com.onlybuns.onlybuns.Model.Location;
import com.onlybuns.onlybuns.Model.Post;
import com.onlybuns.onlybuns.Model.User;
import com.onlybuns.onlybuns.Repository.PostRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;


import java.util.List;
import java.util.Optional;

@Service
public class PostService {
    
    @Autowired
    public PostRepository postRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public Post createPost(Post post) {
       Location location = locationService.createLocation(post.getLocation());

        
       post.setLocation(location);
        User user = userService.findUserByUsername(post.getUsername()).get();
        user.setPostsSeen(user.getPostsSeen()+1);
        userService.Update(user);
        
        return postRepository.save(post);
    }

    @Transactional
    public List<Post> getAllPosts() {
        List<Post> posts =  postRepository.findAllPostsSorted(); // Retrieve all posts
        posts.forEach(post -> Hibernate.initialize(post.getLocation()));
        return posts;
    }

    
    public Optional<Post> getPostById(Long postId) {
        Optional<Post> post = postRepository.findById(postId);
 
        if (post.isPresent()) {

            Location location = locationService.getLocationForPost(postId);
            post.get().setLocation(location); // Set the cached location in the Post object
 
            return post;
       }
 
        return Optional.empty();
    }

    public void markPostAsAdvertisable(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        
        if (!post.isAdvertised())
        {

            post.setAdvertised(true);
            updatePost(postId, post);
            // Prepare message payload
            String message = String.format(
                "Description: %s, Created At: %s, Username: %s",
                post.getDescription(),
                post.getCreationTime(),
                post.getUsername()
            );

            // Send to RabbitMQ
            rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE, "", message);
        }
       
    }

    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Post not found with Id " + id));
        post.setDeleted(true);
        postRepository.save(post);
    }

    @Transactional
    public List<Post> getByUsername(String username) {
        return postRepository.findByUsernameAndDeletedFalse(username); // Retrieve all posts by a specific user
    }

    
    public Optional<Post> updatePost(Long postId,Post updatedPost) {
        Optional<Post> existingPost = postRepository.findById(postId);
        if(existingPost.isPresent())
        {
            Post post = existingPost.get();
            post.setDescription(updatedPost.getDescription());
            post.setLocation(updatedPost.getLocation());
            post.setImage(updatedPost.getImage());
            post.setEdited(true);
            return Optional.of(postRepository.save(post));
        }
        return Optional.empty();
    }

    public List<Post> findPostsNearby(double latitude, double longitude, double radiusKm) {
        List<Post> allPosts = postRepository.findAll();
        return allPosts.stream()
            .filter(post -> {
                if (post.getLocation() == null) return false;
                double dist = haversine(latitude, longitude,
                                        post.getLocation().getLatitude(),
                                        post.getLocation().getLongitude());
                return dist <= radiusKm;
            })
            .toList();
    }

    private double haversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Earth radius in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}
