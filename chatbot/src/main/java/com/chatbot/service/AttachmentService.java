package com.chatbot.service;

import java.util.List;

import com.chatbot.dto.AttachmentDto;

public interface AttachmentService {
	  AttachmentDto saveAttachment(AttachmentDto dto);
	 List<AttachmentDto> getAllAttachments();
}
