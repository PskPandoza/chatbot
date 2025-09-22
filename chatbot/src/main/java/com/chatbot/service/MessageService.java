package com.chatbot.service;

import java.util.List;

import com.chatbot.dto.MessageDto;

public interface MessageService {
	  MessageDto saveMessage(MessageDto messageDto);
	    MessageDto getMessage(Long id);
	    List<MessageDto> getAllMessages();
}
