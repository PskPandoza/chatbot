package com.chatbot.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatbot.dto.ExecutiveDto;
import com.chatbot.service.ExecutiveService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/executives")
@RequiredArgsConstructor
public class ExecutiveController {

    private final ExecutiveService executiveService;

    @PostMapping
    public ResponseEntity<ExecutiveDto> createExecutive(@RequestBody ExecutiveDto dto) {
        return ResponseEntity.ok(executiveService.saveExecutive(dto));
    }

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<ExecutiveDto> getExecutive(@PathVariable String phoneNumber) {
        return ResponseEntity.ok(executiveService.findByPhoneNumber(phoneNumber));
    }

    @GetMapping
    public ResponseEntity<List<ExecutiveDto>> getAllExecutives() {
        return ResponseEntity.ok(executiveService.getAllExecutives());
    }
}
