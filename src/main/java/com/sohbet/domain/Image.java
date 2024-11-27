package com.sohbet.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "image")
public class Image {
	
	@Id
	@GeneratedValue(generator = "uuid") // 12 karakterlik String bir id
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;
	
	private String name;
	
	private String type;
	
	private long length;
	
	private String filePath;
	
//	@OneToOne(mappedBy = "profileImage", fetch = FetchType.LAZY)
//	private User user;
	
	@OneToOne(cascade=CascadeType.ALL)// ImageFile silinirse , imageData da silinsin
	private ImageData imageData; 
	
	public Image(String name, String type,ImageData imageData) {
		this.name = name;
		this.type = type;
		this.imageData = imageData ;
		this.length = imageData.getData().length; // ImageFile  uzunluğu imageData dan çekiliyor
	}


	
	
}

	

	


	





