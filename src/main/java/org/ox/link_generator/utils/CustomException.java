package org.ox.link_generator.utils;

import lombok.*;
import org.springframework.http.*;

@Getter
public class CustomException extends RuntimeException {

	private String message;
	private Object data;
	private HttpStatus status;

	private static final Boolean hasError = true;

	public CustomException(String message, Object data) {
		this.message = message;
		this.data = data;
	}

	public CustomException(HttpStatus status, String message, Object data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public CustomException(HttpStatus status, CustomResponse res) {
		this.status = status;
		this.message = res.getMessage();
		this.data = res.getData();
	}

}