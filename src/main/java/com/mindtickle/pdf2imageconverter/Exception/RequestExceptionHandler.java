package com.mindtickle.pdf2imageconverter.Exception;

import java.util.logging.Level;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import com.mindtickle.pdf2imageconverter.model.Error;
import lombok.extern.java.Log;

@Log
@ControllerAdvice
public class RequestExceptionHandler {
	
	
//	 This class is the comon exit point for all our exception.
//	 Whenever our controller throws an exception this class based on the type of exception gives a response.
	
	@ExceptionHandler(BadRequestException.class)
	protected ResponseEntity<Object> handleBadRequestException(BadRequestException ex, WebRequest request) {
		
		Error error = new Error();
		error.setCode(ex.getStatus().value());
		error.setMessage(ex.getMessage());

		log.log(Level.SEVERE, ex.getMessage());
		return buildResponseEntity(error);
	}
	
	@ExceptionHandler(FileStorageException.class)
	protected ResponseEntity<Object> handleFileStorageException(FileStorageException ex, WebRequest request) {
		
		Error error = new Error();
		error.setCode(ex.getStatus().value());
		error.setMessage(ex.getMessage());

		log.log(Level.SEVERE, ex.getMessage());
		return buildResponseEntity(error);
	}
	
	@ExceptionHandler(ApplicationException.class)
	protected ResponseEntity<Object> handleApplicationException(ApplicationException ex, WebRequest request) {
		
		Error error = new Error();
		error.setCode(ex.getStatus().value());
		error.setMessage(ex.getMessage());

		log.log(Level.SEVERE, ex.getMessage());
		return buildResponseEntity(error);
	}
	
	protected ResponseEntity<Object> buildResponseEntity(Error error) {
		return new ResponseEntity<>(error, error.getStatus());
	}
}
