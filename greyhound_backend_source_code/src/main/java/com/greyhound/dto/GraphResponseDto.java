package com.greyhound.dto;

import java.util.Map;

public class GraphResponseDto {
	private Map<String, Map<String, String>> data;

	public Map<String, Map<String, String>> getData() {
		return data;
	}

	public void setData(Map<String, Map<String, String>> data) {
		this.data = data;
	}

}
