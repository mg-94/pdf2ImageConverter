package com.mindtickle.pdf2imageconverter.Exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FileStorageException extends Exception {

	private final String errorMsg;
	private final HttpStatus status;

	public FileStorageException(String errorMsg) {
		this(HttpStatus.INTERNAL_SERVER_ERROR, errorMsg, null);
	}

	public FileStorageException(String errorMsg, Throwable throwable) {
		this(HttpStatus.INTERNAL_SERVER_ERROR, errorMsg, throwable);
	}

	public FileStorageException(HttpStatus status, String errorMsg, Throwable throwable) {
		super(errorMsg, throwable);
		this.errorMsg = errorMsg;
		this.status = status;
	}

}
