package com.chatbot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chatbot.entity.Executive;

@Repository
public interface ExecutiveRepository extends JpaRepository<Executive, Long> {
    Optional<Executive> findByPhoneNumber(String phoneNumber);
}
