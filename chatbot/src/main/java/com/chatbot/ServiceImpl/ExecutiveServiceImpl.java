package com.chatbot.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.chatbot.dto.ExecutiveDto;
import com.chatbot.entity.Executive;
import com.chatbot.repository.ExecutiveRepository;
import com.chatbot.service.ExecutiveService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExecutiveServiceImpl implements ExecutiveService {
	private final ExecutiveRepository executiveRepository;

	 @Override
	    public ExecutiveDto saveExecutive(ExecutiveDto dto) {
	        Executive executive = Executive.builder()
	                .name(dto.getName())
	                .phoneNumber(dto.getPhoneNumber())
	                .isActive(dto.getIsActive())
	                .build();
	        return mapToDto(executiveRepository.save(executive));
	    }

	    @Override
	    public ExecutiveDto findByPhoneNumber(String phone) {
	        return executiveRepository.findByPhoneNumber(phone)
	                .map(this::mapToDto)
	                .orElse(null);
	    }

	    @Override
	    public List<ExecutiveDto> getAllExecutives() {
	        return executiveRepository.findAll().stream()
	                .map(this::mapToDto)
	                .collect(Collectors.toList());
	    }

	    private ExecutiveDto mapToDto(Executive executive) {
	        return new ExecutiveDto(
	                executive.getId(),
	                executive.getName(),
	                executive.getPhoneNumber(),
	                executive.getIsActive()
	        );
	    }
	}