package com.mindtickle.pdf2imageconverter.Exception;


import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
public class BadRequestException extends Exception {

	private final HttpStatus status;
	private final String errorMsg;

	public BadRequestException(String errorMsg) {
		this(HttpStatus.BAD_REQUEST, errorMsg, null);
	}
	
	public BadRequestException(HttpStatus status, String errorMsg) {
		this(status, errorMsg, null);
	}
	

	public BadRequestException(String errorMsg, Throwable throwable) {
		this(HttpStatus.BAD_REQUEST, errorMsg, throwable);
	}
	
	public BadRequestException(HttpStatus status, String errorMsg, Throwable throwable) {
		super(errorMsg, throwable);
		this.errorMsg = errorMsg;
		this.status = status;
	}




}
