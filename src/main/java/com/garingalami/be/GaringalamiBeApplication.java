package com.garingalami.be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class GaringalamiBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(GaringalamiBeApplication.class, args);
		System.out.println("SERVER IS RUNNING");
	}

	@Bean
	CommandLineRunner recovery(JdbcTemplate jdbcTemplate) {
		return args -> {
			try {
				// Set all products to Active by default for the new logic
				jdbcTemplate.execute("UPDATE products SET active = true WHERE active IS NULL OR active = false");
				System.out.println("✅ DATA RECOVERY: All products initialized to ACTIVE.");
			} catch (Exception e) {
				System.err.println("❌ Recovery script failed: " + e.getMessage());
			}
		};
	}
}
