package com.chatbot.config;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;

@Configuration
@EnableConfigurationProperties(WhatsAppProperties.class)
public class AppConfig {

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @PostConstruct
    public void createUploadDir() throws Exception {
        Path p = Path.of(uploadDir);
        if (!Files.exists(p)) {
            Files.createDirectories(p);
        }
    }
}