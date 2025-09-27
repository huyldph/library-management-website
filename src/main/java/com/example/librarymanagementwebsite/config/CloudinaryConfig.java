package com.example.librarymanagementwebsite.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @NonFinal
    @Value("${cloudinary.cloud-name}")
    protected String CLOUD_NAME;

    @NonFinal
    @Value("${cloudinary.api-key}")
    protected String API_KEY;

    @NonFinal
    @Value("${cloudinary.api-secret}")
    protected String API_SECRET;

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", CLOUD_NAME,
                "api_key", API_KEY,
                "api_secret", API_SECRET));
    }
}
