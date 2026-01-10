package com.security.visitor_gate_pass.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.security.visitor_gate_pass.model.User;
import com.security.visitor_gate_pass.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Admin User
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole("ADMIN");
                userRepository.save(admin);
                System.out.println("✅ Default Admin created: admin / admin123");
            }

            // Guard User
            if (userRepository.findByUsername("guard").isEmpty()) {
                User guard = new User();
                guard.setUsername("guard");
                guard.setPassword(passwordEncoder.encode("guard123"));
                guard.setRole("GUARD");
                userRepository.save(guard);
                System.out.println("✅ Default Guard created: guard / guard123");
            }

            // Regular User (Demo)
            if (userRepository.findByUsername("user").isEmpty()) {
                User user = new User();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("user123"));
                user.setRole("USER");
                userRepository.save(user);
                System.out.println("✅ Default User created: user / user123");
            }
        };
    }
}
