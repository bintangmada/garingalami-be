package com.garingalami.be.controller;

import com.garingalami.be.model.User;
import com.garingalami.be.repository.UserRepository;
import com.garingalami.be.service.JwtService;
import com.garingalami.be.service.AdminLogService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class GoogleAuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AdminLogService adminLogService;

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleLoginRequest request) {
        // Logic: Check if user exists by email or googleId
        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());
        
        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();
            // Update googleId and picture if not present (seamless upgrade)
            boolean updated = false;
            if (user.getGoogleId() == null) {
                user.setGoogleId(request.getGoogleId());
                updated = true;
            }
            if (user.getProfilePicture() == null) {
                user.setProfilePicture(request.getPicture());
                updated = true;
            }
            if (updated) {
                userRepository.save(user);
            }
            adminLogService.log(user.getUsername(), "LOGIN_GOOGLE", "Successful social login");
        } else {
            // Auto-Registration
            user = User.builder()
                    .username(request.getEmail().split("@")[0]) // Simple username from email
                    .email(request.getEmail())
                    .password(java.util.UUID.randomUUID().toString()) // Tambahkan password dummy agar DB tidak error
                    .googleId(request.getGoogleId())
                    .profilePicture(request.getPicture())
                    .role("ROLE_USER")
                    .build();
            userRepository.save(user);
            adminLogService.log(user.getUsername(), "REGISTER_GOOGLE", "New user auto-registered via Google");
        }

        String token = jwtService.generateToken(user.getUsername());
        
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);
        
        return ResponseEntity.ok(response);
    }

    @Data
    public static class GoogleLoginRequest {
        private String email;
        private String name;
        private String googleId;
        private String picture;
    }
}
