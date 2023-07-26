package com.sohbet.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "file_data")
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
   
