package com.example.librarymanagementwebsite.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
            "/api/v1/auth/login",
            "/api/v1/auth/register",
            "/api/v1/auth/refresh",
            "/api/v1/auth/introspect",
            "/api/v1/auth/logout",
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

                .requestMatchers("/api/v1/employee/**").permitAll()
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
