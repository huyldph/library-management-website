package com.example.librarymanagementwebsite.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  // bật Spring Security cho ứng dụng web
@EnableMethodSecurity // cho phép sử dụng @PreAuthorize, @PostAuthorize... trên method
public class SecurityConfig {

    // Các endpoint không cần authen vẫn truy cập được
    private final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/auth/**",
            "/uploads/**",
            "/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**"
    };


    private final CustomJwtDecoder customJwtDecoder;

    public SecurityConfig(CustomJwtDecoder customJwtDecoder) {
        this.customJwtDecoder = customJwtDecoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(request -> request
                .requestMatchers(PUBLIC_ENDPOINTS) // các đường dẫn public
                .permitAll()

                // phân quyền cho các endpoint /api/v1/books
                .requestMatchers(HttpMethod.GET,
                        "/api/v1/books/**"
                )
                .permitAll()
                .requestMatchers(
                        "/api/v1/books/create",
                        "/api/v1/books/update/**",
                        "/api/v1/books/delete/**"
                ).hasAnyRole("STAFF", "ADMIN")

                // phân quyền cho các endpoint /api/v1/categories
                .requestMatchers(HttpMethod.GET,
                        "/api/v1/categories/**"
                )
                .permitAll()
                .requestMatchers(
                        "/api/v1/categories/create",
                        "/api/v1/categories/update/**"
                ).hasAnyRole("STAFF", "ADMIN")

                // phân quyền cho các endpoint /api/v1/publishers
                .requestMatchers(HttpMethod.GET,
                        "/api/v1/publishers/**"
                )
                .permitAll()
                .requestMatchers(
                        "/api/v1/publishers/create",
                        "/api/v1/publishers/update/**"
                ).hasAnyRole("STAFF", "ADMIN")

                // phân quyền cho các endpoint /api/v1/shelves
                .requestMatchers(HttpMethod.GET,
                        "/api/v1/shelves/**"
                )
                .permitAll()
                .requestMatchers(
                        "/api/v1/publishers/create",
                        "/api/v1/publishers/update/**"
                ).hasAnyRole("STAFF", "ADMIN")

                // phân quyền cho các endpoint /api/v1/book-copies
                .requestMatchers(HttpMethod.GET,
                        "/api/v1/book-copies/**"
                )
                .permitAll()
                .requestMatchers(
                        "/api/v1/book-copies/create",
                        "/api/v1/book-copies/update/**"
                ).hasAnyRole("STAFF", "ADMIN")

                // phân quyền cho các endpoint /api/v1/members
                .requestMatchers(
                        "/api/v1/members/{memberId}",
                        "/api/v1/members/create",
                        "/api/v1/members/update/**"
                )
                .permitAll()
                .requestMatchers(
                        "/api/v1/members"
                ).hasAnyRole("STAFF", "ADMIN")

                // phân quyền cho các endpoint /api/v1/loans
                .requestMatchers(HttpMethod.GET,
                        "/api/v1/loans/by-member"
                )
                .permitAll()
                .requestMatchers(
                        "api/v1/loans",
                        "/api/v1/loans/create",
                        "/api/v1/loans/update/**"
                ).hasAnyRole("STAFF", "ADMIN")

                // Các đường dẫn khác cần authen
                .anyRequest()
                .authenticated());

        // Cấu hình Resource Server sử dụng JWT
        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(customJwtDecoder)  // dùng decoder custom để validate token
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())) // convert quyền từ JWT
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())); // custom trả về khi không có quyền

        // Tắt CSRF (thường tắt khi dùng API REST, không cần CSRF token)
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    // Convert claim trong JWT thành GrantedAuthority (role/quyền)
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(""); // bỏ prefix mặc định "ROLE_"

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    // Bean mã hóa mật khẩu, sử dụng BCrypt với strength = 10
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
