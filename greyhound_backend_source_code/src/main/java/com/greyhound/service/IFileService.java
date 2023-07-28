package com.greyhound.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.dto.ImageDto;

/**
 * 
 * @author p4logics
 *
 */
@Service
public interface IFileService {

	void uploadFile(MultipartFile file, String type, ApiResponseDtoBuilder apiResponseDtoBuilder);

	Resource loadFileAsResource(String fileName, String type, String property);

	void uploadMultipleFile(MultipartFile[] files, String type, String fileName,
			ApiResponseDtoBuilder apiResponseDtoBuilder);

	void deleteFile(String type, String file, ApiResponseDtoBuilder apiResponseDtoBuilder);

	String generatePdf(String htmlString, String fileName, String folder);

	ImageDto saveFile(MultipartFile file, String type, String fileName);

}
