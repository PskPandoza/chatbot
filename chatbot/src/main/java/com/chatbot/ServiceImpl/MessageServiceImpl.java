package com.chatbot.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.chatbot.dto.MessageDto;
import com.chatbot.entity.Executive;
import com.chatbot.entity.Message;
import com.chatbot.entity.User;
import com.chatbot.repository.ExecutiveRepository;
import com.chatbot.repository.MessageRepository;
import com.chatbot.repository.UserRepository;
import com.chatbot.service.MessageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ExecutiveRepository executiveRepository;

    @Override
    public MessageDto saveMessage(MessageDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Executive executive = null;
        if (dto.getExecutiveId() != null) {
            executive = executiveRepository.findById(dto.getExecutiveId())
                    .orElseThrow(() -> new RuntimeException("Executive not found"));
        }

        Message message = Message.builder()
                .content(dto.getContent())
                .type(dto.getType())
                .timestamp(dto.getTimestamp())
                .user(user)
                .executive(executive)
                .build();

        return mapToDto(messageRepository.save(message));
    }

    @Override
    public MessageDto getMessage(Long id) {
        return messageRepository.findById(id)
                .map(this::mapToDto)
                .orElse(null);
    }

    @Override
    public List<MessageDto> getAllMessages() {
        return messageRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private MessageDto mapToDto(Message msg) {
        return new MessageDto(
                msg.getId(),
                msg.getContent(),
                msg.getType(),
                msg.getTimestamp(),
                msg.getUser() != null ? msg.getUser().getId() : null,
                msg.getExecutive() != null ? msg.getExecutive().getId() : null
        );
    }
}