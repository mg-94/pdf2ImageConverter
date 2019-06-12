package com.mindtickle.pdf2imageconverter.Exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class ApplicationException extends Exception {
	private final HttpStatus status;
	private final String errorMsg;

	public ApplicationException(String errorMsg) {
		this(HttpStatus.INTERNAL_SERVER_ERROR, errorMsg, null);
	}
	
	public ApplicationException(HttpStatus status, String errorMsg) {
		this(status, errorMsg, null);
	}
	

	public ApplicationException(String errorMsg, Throwable throwable) {
		this(HttpStatus.INTERNAL_SERVER_ERROR, errorMsg, throwable);
	}
	
	public ApplicationException(HttpStatus status, String errorMsg, Throwable throwable) {
		super(errorMsg, throwable);
		this.errorMsg = errorMsg;
		this.status = status;
	}
}
