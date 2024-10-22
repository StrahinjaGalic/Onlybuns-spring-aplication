package com.onlybuns.onlybuns.Service;

import com.onlybuns.onlybuns.Model.User;
import com.onlybuns.onlybuns.Model.UserInfoDetails;
import com.onlybuns.onlybuns.Repository.UserRepository;
import jakarta.mail.MessagingException;
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new UserInfoDetails(user); // Ensure UserInfoDetails implements UserDetails
    }

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
}
