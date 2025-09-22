package com.chatbot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecutiveDto {
	
	    private Long id;
	    private String name;
	    private String phoneNumber;
	    private Boolean isActive;
}
