package com.chatbot.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.chatbot.dto.UserDto;
import com.chatbot.entity.User;
import com.chatbot.repository.UserRepository;
import com.chatbot.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	 private final UserRepository userRepository;
	@Override
	public UserDto saveUser(UserDto userDto) {
		 User user = User.builder()
	                .name(userDto.getName())
	                .phoneNumber(userDto.getPhoneNumber())
	                .email(userDto.getEmail())
	                .build();
	        return mapToDto(userRepository.save(user));
	}

	@Override
	public UserDto findByPhoneNumber(String phone) {
		  return userRepository.findByPhoneNumber(phone)
	                .map(this::mapToDto)
	                .orElse(null);
	    }

	@Override
	public List<UserDto> getAllUsers() {
		return userRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
	 private UserDto mapToDto(User user) {
	        return new UserDto(user.getId(), user.getName(), user.getPhoneNumber(), user.getEmail(),user.getAddress());
	    }
}
