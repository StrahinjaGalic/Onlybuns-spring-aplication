package com.onlybuns.onlybuns.Service;

import com.onlybuns.onlybuns.Dto.UserDto;
import com.onlybuns.onlybuns.Model.Post;
import com.onlybuns.onlybuns.Model.Role;
import com.onlybuns.onlybuns.Model.User;
import com.onlybuns.onlybuns.Model.UserInfoDetails;
import com.onlybuns.onlybuns.Repository.UserRepository;

import io.jsonwebtoken.io.IOException;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PostService postService;

    @Autowired 
    private LikeService likeService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BloomFilter bloomFilter;

    @PostConstruct
    public void init() throws java.io.IOException, MessagingException{
        try{
            // Initialize Bloom Filter with existing usernames
            initializeBloomFilter();
            sendInactiveNotifications();

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * Initialize Bloom Filter with all existing usernames
     */
    private void initializeBloomFilter() {
        List<User> allUsers = userRepository.findAll();
        List<String> usernames = allUsers.stream()
                .map(User::getUsername)
                .toList();
        bloomFilter.initializeWith(usernames);
        System.out.println("Bloom Filter initialized with " + usernames.size() + " usernames");
    }

    public void sendInactiveNotifications() throws java.io.IOException, MessagingException
    {
        List<User> users = userRepository.findAll();

        for(User user : users)
        {
            long days = ChronoUnit.DAYS.between(user.getLastActivity(),LocalDate.now());

            if(days >= 7)
            {
                long likes = 0;
                List<Post> posts = postService.getByUsername(user.getUsername());
                int postNum = postService.getAllPosts().size();
                for(Post post : posts){
                    likes = likes + likeService.countLikesByPost(post.getId());
                }
                
                int notSeen = postNum - user.getPostsSeen();
                emailService.sendInactiveEmail(user.getEmail(),user.getUsername(),likes,notSeen);
            }
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new UserInfoDetails(user); // Ensure UserInfoDetails implements UserDetails
    }

    @Transactional
    public User registerUser(User user) throws Exception {
        // First check Bloom Filter for quick username existence check
        if (bloomFilter.mightContain(user.getUsername())) {
            // Bloom Filter says username might exist, so check database
            if (userRepository.findByUsername(user.getUsername()) != null) {
                throw new Exception("Username is already taken");
            }
            // If we reach here, it was a false positive from Bloom Filter
        }
        // If Bloom Filter says username definitely doesn't exist, skip database check

        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new Exception("Email is already registered");
        }

        String activationToken = UUID.randomUUID().toString();

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(false);
        user.setActivationToken(activationToken);
        user.setLastActivity(LocalDate.now());
        if (user.getRole() == null) {
            user.setRole(Role.USER);
        }
        user.setPostsSeen(0);

        User savedUser = userRepository.save(user);
        
        // Add the new username to Bloom Filter
        bloomFilter.add(user.getUsername());
        
        String activationLink = "http://localhost:8080/api/users/activate?token=" + activationToken;

        sendActivationEmail(savedUser.getEmail(), activationLink);

        return savedUser;
    }

    public Optional<User> findUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    public Optional<User> findUserByEmail(String email) {
        return Optional.ofNullable(userRepository.findByEmail(email));
    }

    public void activateUser(String token) throws Exception {
        Optional<User> userOptional = userRepository.findByActivationToken(token);
        if (!userOptional.isPresent()) {
            throw new Exception("Invalid activation token");
        }

        User user = userOptional.get();
        user.setActive(true);
        user.setActivationToken(null); // Clear the token after activation
        userRepository.save(user);
    }

    private void sendActivationEmail(String email, String link) throws Exception {
        try {
            emailService.sendActivationEmail(email, link);
        } catch (MessagingException e) {
            throw new Exception("Failed to send activation email", e);
        }
    }

    public Optional<String> loginUser(String username, String password) {
        User user_model = userRepository.findByUsername(username);
        Optional<User> user = Optional.ofNullable(user_model);
        

        // Check if user exists and if the password matches
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            // Generate a JWT token using the username and role

            String token = jwtService.generateToken(user.get().getUsername());
            user_model.setLastActivity(LocalDate.now());
            user_model.setPostsSeen(postService.getAllPosts().size());
            userRepository.save(user_model);
          
 
     
            
            return Optional.of(token);
        }

        // Return an empty Optional if authentication fails
        return Optional.empty();
    }
    public List<User> getAll()
    {
        return userRepository.findAll();
    }

    public User Update(User user)
    {
        return userRepository.save(user);
    }

    public User updateUser(String username, UserDto userDto) {
        User existingUser = userRepository.findByUsername(username);

        if(existingUser == null) {
            throw new IllegalArgumentException("User not found");
        }
            
        // Update fields selectively
        existingUser.setName(userDto.name);
        existingUser.setSurname(userDto.surname);
        existingUser.setAddress(userDto.address);

        if (userDto.password != null) {
            existingUser.setPassword(passwordEncoder.encode(userDto.password)); // Encode the password
        }

        return userRepository.save(existingUser);
    }

    /**
     * Check if username is available using Bloom Filter optimization
     */
    public boolean isUsernameAvailable(String username) {
        // First check Bloom Filter
        if (!bloomFilter.mightContain(username)) {
            // Bloom Filter says username definitely doesn't exist, so it's available
            return true;
        }
        
        // Bloom Filter says username might exist, check database for sure
        User existingUser = userRepository.findByUsername(username);
        return existingUser == null;
    }

    /**
     * Get Bloom Filter statistics for monitoring
     */
    public Map<String, Object> getBloomFilterStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("size", bloomFilter.getSize());
        stats.put("approximateElementCount", bloomFilter.getApproximateElementCount());
        stats.put("estimatedFalsePositiveRate", 
                bloomFilter.estimateFalsePositiveRate(bloomFilter.getApproximateElementCount()));
        return stats;
    }

}
