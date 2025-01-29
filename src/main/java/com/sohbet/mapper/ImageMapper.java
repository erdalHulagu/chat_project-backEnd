package com.sohbet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sohbet.DTO.ImageDTO;
import com.sohbet.domain.Image;

@Mapper(componentModel = "spring")
public interface ImageMapper {
	

    // filePath -> url dönüşümü için özel eşleme
    @Mapping(source = "filePath", target = "url")
    ImageDTO imageToImageDTO(Image image);

    // Özel bir yöntem, varsayılan bir değer atamak için kullanılabilir
    default String mapFilePathToUrl(String filePath) {
        return filePath != null ? filePath : "No file path available";
    }

}
