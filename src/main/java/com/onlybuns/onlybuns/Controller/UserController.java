package com.onlybuns.onlybuns.Controller;

import com.onlybuns.onlybuns.Model.Role;
import com.onlybuns.onlybuns.Model.User;
import com.onlybuns.onlybuns.Service.UserService;

import java.util.HashMap;
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

    //accoutn registration
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            return new ResponseEntity<>("Registration successful! Please check your email for the activation link.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //activating user account 
    @GetMapping("/activate")
    public ResponseEntity<String> activateUser(@RequestParam("token") String token) {
        try {
            userService.activateUser(token);  // Assumes you implemented activation logic
            return new ResponseEntity<>("Account activated successfully!", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //basic get methods
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
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        

        // Try to authenticate the user and get a token
        Optional<String> token = userService.loginUser(username, password);
        Optional<User> userOpt = userService.findUserByUsername(username);
        User user = userOpt.get();
        Role role = user.getRole();


        if (token.isPresent()) {
        Map<String, Object> response = new HashMap<>();
        response.put("token", token.get());
        response.put("message", "Login successful");
        response.put("role", role); 

        return ResponseEntity.ok(response);
        } else {
            // Return an error response if authentication fails
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }


}
