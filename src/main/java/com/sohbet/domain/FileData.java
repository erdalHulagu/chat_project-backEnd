package com.sohbet.domain;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fileData")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String filePath;


//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String name;
//    private String type;
//    private String filePath;
//    private long length;
//    
//    @OneToOne(cascade=CascadeType.ALL)// Image silinirse , FileData da silinsin
//	private Image image;
//
//	public FileData(String name, String type,Image image) {
//		this.name = name;
//		this.type = type;
//		this.image = image ;
//		this.length = image.getData().length; // FileData  uzunluğu image dan çekiliyor
//	}

}
   
