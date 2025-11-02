package com.example.demo.config;

import com.example.demo.model.Admin;
import com.example.demo.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner init(AdminRepository repo) {
        return args -> {
            repo.save(new Admin("Aravind", "aravind@example.com", 26));
            repo.save(new Admin("Sebastian", "sebastian@example.com", 30));
        };
    }
}
