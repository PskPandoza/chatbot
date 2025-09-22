package com.chatbot.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.chatbot.dto.AttachmentDto;
import com.chatbot.entity.Attachment;
import com.chatbot.entity.Message;
import com.chatbot.repository.AttachmentRepository;
import com.chatbot.repository.MessageRepository;
import com.chatbot.service.AttachmentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final MessageRepository messageRepository;

    @Override
    public AttachmentDto saveAttachment(AttachmentDto dto) {
        Message message = messageRepository.findById(dto.getMessageId())
                .orElseThrow(() -> new RuntimeException("Message not found"));

        Attachment attachment = Attachment.builder()
                .fileName(dto.getFileName())
                .fileType(dto.getFileType())
                .fileUrl(dto.getFileUrl())
                .message(message)
                .build();

        return mapToDto(attachmentRepository.save(attachment));
    }

    @Override
    public List<AttachmentDto> getAllAttachments() {
        return attachmentRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private AttachmentDto mapToDto(Attachment att) {
        return new AttachmentDto(
                att.getId(),
                att.getFileName(),
                att.getFileType(),
                att.getFileUrl(),
                att.getMessage() != null ? att.getMessage().getId() : null
        );
    }
}

