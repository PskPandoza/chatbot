package com.chatbot.service;

import java.util.List;

import com.chatbot.dto.ExecutiveDto;

public interface ExecutiveService {
	  ExecutiveDto saveExecutive(ExecutiveDto executiveDto);
	    ExecutiveDto findByPhoneNumber(String phone);
	    List<ExecutiveDto> getAllExecutives();
}
