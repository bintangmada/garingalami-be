package com.garingalami.be.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(name = "google_id", unique = true)
    private String googleId;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(nullable = false)
    private String role; // e.g., "ROLE_USER", "ROLE_ADMIN"
}
