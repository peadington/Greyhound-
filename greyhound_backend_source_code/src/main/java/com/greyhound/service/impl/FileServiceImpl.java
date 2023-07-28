package com.greyhound.service.impl;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.html2pdf.HtmlConverter;
import com.greyhound.constant.AppConstant;
import com.greyhound.constant.Constants;
import com.greyhound.dto.ApiResponseDto.ApiResponseDtoBuilder;
import com.greyhound.dto.ImageDto;
import com.greyhound.exception.FileStorageException;
import com.greyhound.exception.MyFileNotFoundException;
import com.greyhound.property.FileStorageProperties;
import com.greyhound.service.IFileService;

/**
 * 
 * @author p4logics
 *
 */
@Service
public class FileServiceImpl implements IFileService {

	private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	private Path fileStorageLocation;

	@Autowired
	private Environment environment;

	@Autowired
	public FileServiceImpl(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException(
					Constants.COULD_NOT_CREATE_THE_DIRECTORY_WHERE_THE_UPLOAD_FILES_WILL_BE_STORED, ex);
		}
	}

	@Override
	public void uploadFile(MultipartFile file, String type, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		UUID uuid = UUID.randomUUID();
		String fileName = uuid.toString();
		ImageDto imageDto = storeFile(file, fileName, type);
		apiResponseDtoBuilder.withMessage(Constants.FILE_UPLOAD_SUCCESSFULLY).withStatus(HttpStatus.OK)
				.withData(imageDto);
	}

	@Override
	public ImageDto saveFile(MultipartFile file, String type, String fileName) {
		return storeFile(file, fileName, type);
	}

	@Override
	public void uploadMultipleFile(MultipartFile[] files, String type, String fileName,
			ApiResponseDtoBuilder apiResponseDtoBuilder) {
		List<ImageDto> imageList = new ArrayList<ImageDto>();
		int count = 1;
		for (MultipartFile file : files) {
			imageList.add(storeFile(file, new Date().getTime() + "_" + count, type));
			count++;
		}
		apiResponseDtoBuilder.withMessage(Constants.FILE_UPLOAD_SUCCESSFULLY).withStatus(HttpStatus.OK)
				.withData(imageList);
	}

	public ImageDto storeFile(MultipartFile file, String fileName, String imageType) {
		ImageDto imageDto = new ImageDto();
		// Normalize file name
		String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());
		File fileDirectory = new File(
				environment.getProperty(AppConstant.FILE_UPLOAD_DIR) + File.separator + imageType);
		if (!fileDirectory.isDirectory()) {
			fileDirectory.mkdir();
		}
		fileStorageLocation = Paths
				.get(environment.getProperty(AppConstant.FILE_UPLOAD_DIR) + File.separator + imageType).toAbsolutePath()
				.normalize();
		originalFileName = fileName + "." + extension;
		try {
			// Check if the file's name contains invalid characters
			if (originalFileName.contains("..")) {
				throw new FileStorageException(Constants.FILENAME_CONTAINS_INVALID_PATH_SEQUENCE + originalFileName);
			}

			// Copy file to the target location (Replacing existing file with the same name)

			Path targetLocation = this.fileStorageLocation.resolve(originalFileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			imageDto.setOriginalUrl(imageType + File.separator + originalFileName);
			// Create thumbnail image
			if (extension.equalsIgnoreCase(AppConstant.JPG) || extension.equalsIgnoreCase(AppConstant.PNG)) {
				BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
				img.createGraphics()
						.drawImage(ImageIO
								.read(new File(environment.getProperty(AppConstant.FILE_UPLOAD_DIR) + File.separator
										+ imageType + File.separator + originalFileName))
								.getScaledInstance(100, 100, Image.SCALE_SMOOTH), 0, 0, null);
				ImageIO.write(img, extension, new File(environment.getProperty(AppConstant.FILE_UPLOAD_DIR)
						+ File.separator + imageType + File.separator + fileName + "_thumb." + extension));
				imageDto.setThumbnailUrl(imageType + File.separator + fileName + "_thumb." + extension);
			}
			return imageDto;
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + originalFileName + ". Please try again!", ex);
		}
	}

	public Resource loadFileAsResource(String fileName, String type, String directory) {
		try {
			Path filePath = Paths.get(directory + File.separator + type).toAbsolutePath().normalize().resolve(fileName)
					.normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException(Constants.FILE_NOT_FOUND + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException(Constants.FILE_NOT_FOUND + fileName, ex);
		}
	}

	@Override
	public void deleteFile(String type, String fileName, ApiResponseDtoBuilder apiResponseDtoBuilder) {
		File file = new File(environment.getProperty(AppConstant.FILE_UPLOAD_DIR) + File.separator + type
				+ File.separator + fileName);
		String fileTimeTemp = file.getName().split("\\.")[0];
		String extension = FilenameUtils.getExtension(file.getName());
		if (file.exists()) {
			file.delete();
			if (extension.equals("jpg") || extension.equals("png")) {
				File thumbFile = new File(environment.getProperty(AppConstant.FILE_UPLOAD_DIR) + File.separator + type
						+ File.separator + fileTimeTemp + "_thumb." + extension);
				if (thumbFile.exists()) {
					thumbFile.delete();
				}
			}
			apiResponseDtoBuilder.withMessage(Constants.FILE_DELETED_SUCCESSFULLY).withStatus(HttpStatus.OK);
			logger.info("File Delete Success!! File is " + fileName);
		} else {
			apiResponseDtoBuilder.withMessage(Constants.FILE_NOT_FOUND).withStatus(HttpStatus.OK);
			logger.error("File Delete Fail !! File not found!! File is" + fileName);
		}
	}

	@Override
	public String generatePdf(String htmlString, String fileName, String folder) {
		String fileUrl = folder;
		fileName += ".pdf";
		try {
			String fileDirectory = environment.getProperty(AppConstant.FILE_UPLOAD_DIR) + File.separator + fileUrl;
			File file = new File(fileDirectory);
			if (!file.isDirectory()) {
				file.mkdir();
			}
			fileUrl += File.separator + fileName;
			fileDirectory += File.separator + fileName;
			HtmlConverter.convertToPdf(htmlString, new FileOutputStream(fileDirectory));
			logger.info("Generte PDF Success!! " + fileDirectory);
		} catch (FileNotFoundException e) {
			logger.error("Generte PDF Fail!! File Not Exception " + e.getMessage());
		} catch (IOException e) {
			logger.error("Generte PDF Fail!! IOException " + e.getMessage());
		}
		return fileUrl;
	}
}
