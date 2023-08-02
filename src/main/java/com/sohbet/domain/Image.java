package com.sohbet.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "image")
public class Image {
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	@Lob
	 @Column(name = "imagedata",length = 1000)
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

