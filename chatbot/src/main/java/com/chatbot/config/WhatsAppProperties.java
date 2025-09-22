package com.chatbot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
@ConfigurationProperties("whatsapp")

public class WhatsAppProperties {
    private String apiUrl;
    private String phoneNumberId;
    private String accessToken;
    private String businessAccountId;
    private String verifyToken;
    private String adminNumbers; // comma-separated
}

