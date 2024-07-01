package com.sohbet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sohbet.domain.Chat;
import com.sohbet.domain.User;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>{
	
	
	@Modifying
	@Query("SELECT c from Chat c join c.users u where u.id=:userId")
	public List<Chat> findChatByUserId(@Param("userId") Long userId);

//	@Query("Select c from Chat where c.isGroup=false and :user Member of c.users and :id Member of c.id")
//     public Chat findSingleChatByuserIds(@Param("user") User user,
//    		                              @Param("userId") Long userId);
	@Modifying
	@Query("SELECT c FROM Chat c WHERE c.isGroup = false AND :user MEMBER OF c.users AND :currentUser MEMBER OF c.users")
	public Chat findSingleChatByUserIds(@Param("user") User user, 
			                            @Param("currentUser") User currentUser);

	
}

