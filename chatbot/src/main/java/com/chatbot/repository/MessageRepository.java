package com.chatbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chatbot.entity.Message;


@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}
