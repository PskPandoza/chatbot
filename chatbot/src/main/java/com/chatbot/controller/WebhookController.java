package com.chatbot.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.chatbot.ServiceImpl.WhatsAppService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final WhatsAppService whatsAppService;

    // ✅ WhatsApp Verification Callback
    @GetMapping
    public ResponseEntity<String> verifyWebhook(
            @RequestParam(name = "hub.mode", required = false) String mode,
            @RequestParam(name = "hub.verify_token", required = false) String verifyToken,
            @RequestParam(name = "hub.challenge", required = false) String challenge) {

        String response = whatsAppService.verifyWebhook(mode, verifyToken, challenge);
        if (response != null) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(403).body("Verification failed");
    }

    // ✅ WhatsApp Incoming Messages
    @PostMapping
    public ResponseEntity<Void> receiveMessage(@RequestBody Map<String, Object> payload) {
        whatsAppService.processWebhookEvent(payload);
        return ResponseEntity.ok().build();
    }
}
