package com.garingalami.be.controller;

import com.garingalami.be.model.User;
import com.garingalami.be.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        
        if (user.isPresent() && user.get().getPassword().equals(request.getPassword())) {
            // In a real app, we'd return a JWT token here.
            // For now, we return the user info as a simple "Authenticated" signal.
            return ResponseEntity.ok(user.get());
        }
        
        return ResponseEntity.status(401).body("Invalid username or password");
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
}
