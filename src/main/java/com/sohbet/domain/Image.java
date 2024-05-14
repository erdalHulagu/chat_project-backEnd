package com.sohbet.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "image")
public class Image {
	

	@Id
	@GeneratedValue(strategy = GenerationType.UUID) 
	private String id;

	@Lob
	@Column(name = "imagedata",length = 5000)
	private byte[] data;

	@NotBlank(message = "Please provide a name.")
	@Column(name = "name")
	private String name;

	@Column(name = "type")
	private String type;


	public Image(byte[] data) {
		this.data = data;
	}

	public Image(String id) {
		this.id = id;
	}

	

	


	



}

