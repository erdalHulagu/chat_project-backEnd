package com.sohbet.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;



import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "image")
public class Image {
	

	@Id
	@GeneratedValue(strategy = GenerationType.UUID) 
	private String id;
	
	private String name;
	
	private String type;
	
	private long length;
	
	private String filePath;
	
	@OneToOne(cascade=CascadeType.ALL)// ImageFile silinirse , imageData da silinsin
	private ImageData imageData; 
	
	public Image(String name, String type,ImageData imageData) {
		this.name = name;
		this.type = type;
		this.imageData = imageData ;
		this.length = imageData.getData().length; // ImageFile  uzunluğu imageData dan çekiliyor
	}


	
	
}

	

	


	





