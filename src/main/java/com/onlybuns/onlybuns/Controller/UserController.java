package com.onlybuns.onlybuns.Controller;

import com.onlybuns.onlybuns.Dto.UserDto;
import com.onlybuns.onlybuns.Model.Role;
import com.onlybuns.onlybuns.Model.User;
import com.onlybuns.onlybuns.Service.LoginLimiterService;
import com.onlybuns.onlybuns.Service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LoginLimiterService rateLimiterService;

    // accoutn registration
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            return new ResponseEntity<>("Registration successful! Please check your email for the activation link.",
                    HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // activating user account
    @GetMapping("/activate")
    public ResponseEntity<String> activateUser(@RequestParam("token") String token) {
        try {
            userService.activateUser(token); // Assumes you implemented activation logic
            return new ResponseEntity<>("Account activated successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // basic get methods
    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        return userService.findUserByUsername(username)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.findUserByEmail(email)
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest, HttpServletRequest request) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        // Get IP address from the request
        String ipAddress = request.getRemoteAddr();
        System.out.println(ipAddress);

        // Check if the IP address is allowed to make login attempts
        if (!rateLimiterService.isAllowed(ipAddress)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many login attempts. Please try again later.");
        }

        Optional<User> userOpt = userService.findUserByUsername(username);

        // Check if the user exists and is active
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (!user.isActive()) {
                // Return a forbidden response if the account is inactive
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Account is inactive. Please contact support.");
            }

            // Authenticate the user and get a token if active
            Optional<String> token = userService.loginUser(username, password);
            Role role = user.getRole();

            if (token.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("token", token.get());
                response.put("message", "Login successful");
                response.put("role", role);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> allUsers = userService.getAll();
        return ResponseEntity.ok(allUsers);
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<String> updateUser(@PathVariable String username, @RequestBody UserDto userDto) {
        try {
            User updatedUser = userService.updateUser(username, userDto);
            return new ResponseEntity<>("User updated successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/check-username/{username}")
    public ResponseEntity<Map<String, Object>> checkUsernameAvailability(@PathVariable String username) {
        Map<String, Object> response = new HashMap<>();
        boolean isAvailable = userService.isUsernameAvailable(username);
        
        response.put("username", username);
        response.put("available", isAvailable);
        response.put("usedBloomFilter", true);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/bloom-filter/stats")
    public ResponseEntity<Map<String, Object>> getBloomFilterStats() {
        Map<String, Object> stats = userService.getBloomFilterStats();
        return ResponseEntity.ok(stats);
    }

}
