package com.chatbot.service;

import java.util.List;

import com.chatbot.dto.UserDto;

public interface UserService {
	UserDto saveUser(UserDto userDto);
    UserDto findByPhoneNumber(String phone);
    List<UserDto> getAllUsers();
}
