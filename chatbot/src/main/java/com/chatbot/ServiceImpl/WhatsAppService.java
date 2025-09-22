package com.chatbot.ServiceImpl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.chatbot.config.WhatsAppProperties;
import com.chatbot.entity.Attachment;
import com.chatbot.entity.Message;
import com.chatbot.entity.User;
import com.chatbot.repository.AttachmentRepository;
import com.chatbot.repository.MessageRepository;
import com.chatbot.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class WhatsAppService {

    private final WhatsAppProperties props;
    private final RestTemplate restTemplate;
    private final MessageRepository messageRepository;
    private final AttachmentRepository attachmentRepository;
    private final UserRepository userRepository;

    // 1) Verify webhook: returns the challenge if verified, otherwise null
    public String verifyWebhook(String mode, String verifyToken, String challenge) {
        log.info("verifyWebhook called mode={}, token={}", mode, verifyToken);
        if ("subscribe".equals(mode) && props.getVerifyToken() != null &&
                props.getVerifyToken().equals(verifyToken)) {
            log.info("Webhook verification success");
            return challenge;
        }
        log.warn("Webhook verification failed");
        return null;
    }

    // 2) Process incoming webhook event (payload is the JSON body as Map)
    @SuppressWarnings("unchecked")
    public void processWebhookEvent(Map<String, Object> payload) {
        log.info("Received webhook payload: {}", payload);

        try {
            List<Map<String, Object>> entries = (List<Map<String, Object>>) payload.get("entry");
            if (entries == null || entries.isEmpty()) {
                log.debug("No entry in payload");
                return;
            }
            Map<String, Object> firstEntry = entries.get(0);
            List<Map<String, Object>> changes = (List<Map<String, Object>>) firstEntry.get("changes");
            if (changes == null || changes.isEmpty()) {
                log.debug("No changes in entry");
                return;
            }
            Map<String, Object> change = changes.get(0);
            Map<String, Object> value = (Map<String, Object>) change.get("value");

            if (value == null) {
                log.debug("No value in change");
                return;
            }

            // messages (incoming)
            List<Map<String, Object>> messages = (List<Map<String, Object>>) value.get("messages");
            if (messages == null || messages.isEmpty()) {
                log.debug("No messages in value");
                return;
            }

            Map<String, Object> message = messages.get(0);
            String from = (String) message.get("from");      // sender phone (e.g. 91XXXXXXXXXX)
            String msgId = (String) message.get("id");
            String type = (String) message.get("type");

            // create or find User
            User user = userRepository.findByPhoneNumber(from)
                    .orElseGet(() -> {
                        User u = new User();
                        u.setPhoneNumber(from);
                        u.setName(null);
                        return userRepository.save(u);
                    });

            String text = null;
            if ("text".equals(type)) {
                Map<String, Object> textObj = (Map<String, Object>) message.get("text");
                if (textObj != null) text = (String) textObj.get("body");
            }

            // Save message record
            Message msgEntity = new Message();
            msgEntity.setContent(text);
            msgEntity.setTimestamp(LocalDateTime.now());
            msgEntity.setUser(user);
            messageRepository.save(msgEntity);
            log.info("Saved message id={} from={} type={}", msgId, from, type);

            // If media (image/video), download and save an Attachment record
            if ("image".equals(type) || "video".equals(type) || "audio".equals(type) || "document".equals(type)) {
                Map<String, Object> mediaObj = (Map<String, Object>) message.get(type);
                if (mediaObj != null && mediaObj.containsKey("id")) {
                    String mediaId = (String) mediaObj.get("id");
                    String mimeType = (String) mediaObj.get("mime_type");
                    // 1. call GET /{mediaId} to get download url
                    String fileUrl = getMediaUrl(mediaId);
                    if (fileUrl != null) {
                        // 2. download file bytes using the same access token
                        String savedPath = downloadAndSave(fileUrl, mediaId, mimeType);
                        // 3. save attachment record
                        Attachment att = new Attachment();
                        att.setFileName(Path.of(savedPath).getFileName().toString());
                        att.setFileType(mimeType);
                        att.setFileUrl(savedPath);
                        att.setMessage(msgEntity);
                        attachmentRepository.save(att);
                        log.info("Saved attachment {} for message {}", savedPath, msgEntity.getId());
                    } else {
                        log.warn("Could not obtain file URL for mediaId={}", mediaId);
                    }
                }
            }

            // TODO: apply your chatbot state machine logic here, e.g. check session and reply accordingly
            // For example: if text == "hi" call sendTextMessage(from, menu)
        } catch (Exception ex) {
            log.error("Error while processing webhook event", ex);
        }
    }

    // 3) Send a text message via WhatsApp Cloud API
    public void sendTextMessage(String to, String bodyText) {
        String url = String.format("%s/%s/messages", props.getApiUrl(), props.getPhoneNumberId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(props.getAccessToken());

        Map<String, Object> textPayload = Map.of("body", bodyText);
        Map<String, Object> payload = Map.of(
                "messaging_product", "whatsapp",
                "to", to,
                "type", "text",
                "text", textPayload
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        try {
            ResponseEntity<String> resp = restTemplate.postForEntity(url, request, String.class);
            log.info("sendTextMessage to {} returned status {}", to, resp.getStatusCode());
            if (!resp.getStatusCode().is2xxSuccessful()) {
                log.warn("WhatsApp API returned non-2xx: body={}", resp.getBody());
            }
        } catch (Exception ex) {
            log.error("Error sending whatsapp message to {}: {}", to, ex.getMessage(), ex);
        }
    }

    // Helper: Call GET /{mediaId} to get the file URL (graph endpoint)
    @SuppressWarnings("unchecked")
    private String getMediaUrl(String mediaId) {
        try {
            String url = String.format("%s/%s", props.getApiUrl(), mediaId);
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(props.getAccessToken());
            HttpEntity<Void> req = new HttpEntity<>(headers);
            ResponseEntity<Map> resp = restTemplate.exchange(url, HttpMethod.GET, req, Map.class);
            if (resp.getStatusCode().is2xxSuccessful() && resp.getBody() != null) {
                // response contains "url"
                Object u = resp.getBody().get("url");
                if (u != null) return u.toString();
            } else {
                log.warn("getMediaUrl returned {} {}", resp.getStatusCode(), resp.getBody());
            }
        } catch (Exception ex) {
            log.error("Failed to get media URL for mediaId {}: {}", mediaId, ex.getMessage(), ex);
        }
        return null;
    }

    // Helper: Download bytes from the fileUrl and save to local disk; returns saved path
    private String downloadAndSave(String fileUrl, String mediaId, String mimeType) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(props.getAccessToken()); // fileUrl still needs token
            HttpEntity<Void> req = new HttpEntity<>(headers);
            ResponseEntity<byte[]> resp = restTemplate.exchange(fileUrl, HttpMethod.GET, req, byte[].class);

            if (!resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null) {
                log.warn("Failed to download media file: status={}", resp.getStatusCode());
                return null;
            }

            byte[] fileBytes = resp.getBody();
            String ext = guessExtension(mimeType);
            Path out = Path.of("uploads", mediaId + (ext != null ? ("." + ext) : ""));
            Files.write(out, fileBytes);
            return out.toAbsolutePath().toString();
        } catch (Exception ex) {
            log.error("Error downloading media from {}: {}", fileUrl, ex.getMessage(), ex);
            return null;
        }
    }

    private String guessExtension(String mimeType) {
        if (mimeType == null) return null;
        if (mimeType.contains("jpeg") || mimeType.contains("jpg")) return "jpg";
        if (mimeType.contains("png")) return "png";
        if (mimeType.contains("mp4")) return "mp4";
        if (mimeType.contains("mpeg") || mimeType.contains("audio")) return "mp3";
        return null;
    }
}
