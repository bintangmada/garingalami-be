package com.garingalami.be.config;

import com.garingalami.be.model.User;
import com.garingalami.be.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                User admin = User.builder()
                        .username("admin")
                        .password("bintangmada")
                        .role("ROLE_ADMIN")
                        .build();
                userRepository.save(admin);
                System.out.println("Default Admin created: admin / bintangmada");
            }
        };
    }
}
