package com.example.librarymanagementwebsite.config;

import com.example.librarymanagementwebsite.account.Role;
import com.example.librarymanagementwebsite.account.RoleRepository;
import com.example.librarymanagementwebsite.account.User;
import com.example.librarymanagementwebsite.account.UserRepository;
import com.example.librarymanagementwebsite.constant.PredefinedRole;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    static final String ADMIN_USER_NAME = "admin";
    static final String ADMIN_PASSWORD = "admin";

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driverClassName",
            havingValue = "org.postgresql.Driver")
    ApplicationRunner applicationRunner(
            UserRepository userRepository, RoleRepository roleRepository) {
        log.info("Initializing application.....");
        return args -> {
            // 1. Kiểm tra hoặc tạo role ADMIN
            Role adminRole = roleRepository.findByRoleName(PredefinedRole.ADMIN_ROLE)
                    .orElseGet(() -> {
                        log.info("Creating default ADMIN role...");
                        return roleRepository.save(Role.builder()
                                .roleName(PredefinedRole.ADMIN_ROLE)
                                .description("Admin role")
                                .build());
                    });
            Role staffRole = roleRepository.findByRoleName(PredefinedRole.STAFF_ROLE)
                    .orElseGet(() -> {
                        log.info("Creating default STAFF role...");
                        return roleRepository.save(Role.builder()
                                .roleName(PredefinedRole.STAFF_ROLE)
                                .description("Staff role")
                                .build());
                    });

            // 2. Kiểm tra hoặc tạo user ADMIN
            if (userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {
                log.info("Creating default ADMIN user...");
                User userAccount = User.builder()
                        .username(ADMIN_USER_NAME)
                        .passwordHash(passwordEncoder.encode(ADMIN_PASSWORD))
                        .fullName("Admin User")
                        .email("ledinhhuy26032004@gmail.com")
                        .role(adminRole)
                        .build();
                userRepository.save(userAccount);
                log.info("Default ADMIN user created successfully!");
            } else {
                log.info("ADMIN user already exists. Skipping creation.");
            }
        };
    }
}
