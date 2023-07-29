package com.sohbet.request;

import java.util.List;
import java.util.Set;

import com.sohbet.domain.Image;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupChatRequest {
	private List<Long> userIds;
	
	private String chatName;
	
	private Set<Image> chatImage;

}
