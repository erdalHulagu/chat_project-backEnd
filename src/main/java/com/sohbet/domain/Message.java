package com.sohbet.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "message")
@Entity
public class Message {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private String content;
	
	@Column
	private LocalDateTime createAt;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "chatId")
	private Chat chat;
	
	
	

}
