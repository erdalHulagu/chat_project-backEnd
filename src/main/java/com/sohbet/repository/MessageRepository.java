package com.sohbet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sohbet.domain.Message;

public interface MessageRepository extends JpaRepository<Message, Long>{
	
	@Query("SELECT m FROM Message m join m.chat c where c.id=:chatId")
	public List<Message> findByChatId(@Param("chatId") Long chatId);

}
