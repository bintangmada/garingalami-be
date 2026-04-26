package com.garingalami.be.config;

import com.garingalami.be.model.Product;
import com.garingalami.be.model.User;
import com.garingalami.be.repository.ProductRepository;
import com.garingalami.be.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepository, ProductRepository productRepository) {
        return args -> {
            // Seed Admin User
            if (userRepository.count() == 0) {
                User admin = User.builder()
                        .username("admin")
                        .password("bintangmada")
                        .role("ROLE_ADMIN")
                        .build();
                userRepository.save(admin);
                System.out.println("Default Admin created: admin / bintangmada");
            }

            // Seed Initial Products
            if (productRepository.count() == 0) {
                String[] names = {
                    "Premium Dried Mango", "Golden Dried Pineapple", "Crunchy Dragon Fruit",
                    "Sweet Jackfruit Crisps", "Dried Papaya Spears", "Natural Banana Chips",
                    "Zesty Dried Strawberry", "Vibrant Kiwi Slices", "Crunchy Apple Chips",
                    "Toasted Coconut Flakes", "Pink Guava Spears", "Signature Snake Fruit",
                    "Artisan Dried Apple"
                };
                String[] categories = {
                    "Classic", "Classic", "Exotic", "Crunchy", "Wellness", "Crunchy",
                    "Classic", "Exotic", "Crunchy", "Exotic", "Wellness", "Classic", "Wellness"
                };
                double[] prices = {
                    45000, 38000, 52000, 42000, 35000, 30000, 55000, 48000, 32000, 28000, 46000, 50000, 36000
                };
                String[] images = {
                    "assets/products/mango.png", "assets/products/pineapple.png", "assets/products/dragonfruit.png",
                    "assets/products/jackfruit.png", "assets/products/papaya.png", "assets/products/banana.png",
                    "assets/products/strawberry.png", "assets/products/kiwi.png", "assets/products/apple.png",
                    "assets/products/coconut.png", "assets/products/guava.png", "assets/products/salak.png",
                    "assets/products/apple_chewy.png"
                };

                for (int i = 0; i < names.length; i++) {
                    Product product = Product.builder()
                            .name(names[i])
                            .category(categories[i])
                            .price(BigDecimal.valueOf(prices[i]))
                            .quota(100) // Default quota
                            .mainImageUrl(images[i])
                            .description("Hand-selected and slowly preserved to honor its original vibration and essence.")
                            .build();
                    productRepository.save(product);
                }
                System.out.println("Seeded " + names.length + " initial products.");
            }
        };
    }
}
