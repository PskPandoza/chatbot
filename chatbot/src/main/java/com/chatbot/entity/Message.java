package com.chatbot.entity;

import java.time.LocalDateTime;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
	 @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String content; // Text message
	    private String type;    // text, image, video
	    private LocalDateTime timestamp;

	    @ManyToOne
	    @JoinColumn(name = "user_id")
	    private User user; // Sender/Receiver

	    @ManyToOne
	    @JoinColumn(name = "executive_id")
	    private Executive executive; // Assigned executive

	    @OneToOne(mappedBy = "message", cascade = CascadeType.ALL)
	    private Attachment attachment;
	}
