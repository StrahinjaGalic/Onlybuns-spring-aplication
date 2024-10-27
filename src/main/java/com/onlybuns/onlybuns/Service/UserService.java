package com.onlybuns.onlybuns.Service;

import com.onlybuns.onlybuns.Model.Role;
import com.onlybuns.onlybuns.Model.User;
import com.onlybuns.onlybuns.Model.UserInfoDetails;
import com.onlybuns.onlybuns.Repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    private JwtService jwtService;

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
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new Exception("Username is already taken");
        }

        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new Exception("Email is already registered");
        }

        String activationToken = UUID.randomUUID().toString();

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(false);
        user.setActivationToken(activationToken);
        if (user.getRole() == null) {
            user.setRole(Role.USER);
                }

        User savedUser = userRepository.save(user);
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
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));

        // Check if user exists and if the password matches
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            // Generate a JWT token using the username and role
            String token = jwtService.generateToken(user.get().getUsername());
            return Optional.of(token);
        }

        // Return an empty Optional if authentication fails
        return Optional.empty();
    }




}
