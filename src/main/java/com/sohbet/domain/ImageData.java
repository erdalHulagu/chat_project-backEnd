package com.sohbet.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Table(name = "file_data")
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class ImageData {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Lob
	private byte[] data; // image datası byte array şeklinde
	
	public ImageData(byte[] data){
		this.data= data;
	}
	
	public ImageData(Long id) {
		this.id=id;
	}

}
   
