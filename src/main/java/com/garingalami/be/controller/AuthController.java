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
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private com.garingalami.be.service.AdminLogService adminLogService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> user = userRepository.findByUsername(request.getUsername());
        
        if (user.isPresent() && user.get().getPassword().equals(request.getPassword())) {
            adminLogService.log(request.getUsername(), "LOGIN", "Successful login to Atelier Console");
            return ResponseEntity.ok(user.get());
        }
        
        adminLogService.log(request.getUsername(), "LOGIN_FAILED", "Invalid credentials attempt for user: " + request.getUsername());
        return ResponseEntity.status(401).body("Invalid username or password");
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam(required = false) String user) {
        String adminUser = (user != null) ? user : "Admin";
        try {
            adminLogService.log(adminUser, "LOGOUT", "Session terminated by user");
            return ResponseEntity.ok("Logout recorded");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }
}
