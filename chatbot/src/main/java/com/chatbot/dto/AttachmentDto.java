package com.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachmentDto {
	 private Long id;
	    private String fileName;
	    private String fileType;
	    private String fileUrl;
	    private Long messageId;
}
