package com.chatbot.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatbot.dto.AttachmentDto;
import com.chatbot.service.AttachmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping
    public ResponseEntity<AttachmentDto> saveAttachment(@RequestBody AttachmentDto dto) {
        return ResponseEntity.ok(attachmentService.saveAttachment(dto));
    }

    @GetMapping
    public ResponseEntity<List<AttachmentDto>> getAllAttachments() {
        return ResponseEntity.ok(attachmentService.getAllAttachments());
    }
}