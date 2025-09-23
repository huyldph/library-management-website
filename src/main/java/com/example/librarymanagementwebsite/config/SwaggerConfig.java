package com.example.librarymanagementwebsite.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI yellowCatOpenAPI() {
        final String schemeName = "bearer-jwt"; // Đặt tên scheme cho security

        return new OpenAPI()
                // 📝 Thông tin chung hiển thị trên Swagger UI
                .info(new Info()
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("library management website")
                                .email("ledinhhuy26032004@gmail.com")))
                // 🔑 Khai báo yêu cầu security (JWT Bearer)
                .addSecurityItem(new SecurityRequirement().addList(schemeName))
                // 🔧 Cấu hình chi tiết cho security scheme
                .components(new Components()
                        .addSecuritySchemes(schemeName,
                                new SecurityScheme()
                                        .name(schemeName)                 // Tên scheme
                                        .type(SecurityScheme.Type.HTTP)   // Kiểu HTTP auth
                                        .scheme("bearer")                 // Scheme Bearer
                                        .bearerFormat("JWT")              // Token format: JWT
                                        .description("Dán access-token (không cần tiền tố Bearer )")
                        ));
    }
}