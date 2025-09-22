package com.chatbot.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
	 private Long id;
	    private String content;
	    private String type;
	    private LocalDateTime timestamp;
	    private Long userId;
	    private Long executiveId;
}
