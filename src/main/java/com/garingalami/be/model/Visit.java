package com.garingalami.be.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "visits")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    private String path;

    private String ipHash; // For unique visitor counting without storing PII

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
