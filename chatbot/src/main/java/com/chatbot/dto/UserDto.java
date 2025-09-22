package com.chatbot.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDto {
	
	 private Long id;

	    private String name;

	    @Column(unique = true, nullable = false)
	    private String phoneNumber;  // WhatsApp number

	    private String email;
	    private String address;


}
