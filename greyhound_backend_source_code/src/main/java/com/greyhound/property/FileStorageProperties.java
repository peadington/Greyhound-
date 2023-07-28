package com.greyhound.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 
 * @author p4logics
 *
 */
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
	private String uploadDir;

	public String getUploadDir() {
		return uploadDir;
	}

	public void setUploadDir(String uploadDir) {
		this.uploadDir = uploadDir;
	}
}
