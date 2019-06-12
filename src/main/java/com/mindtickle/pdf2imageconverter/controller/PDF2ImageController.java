package com.mindtickle.pdf2imageconverter.controller;

import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mindtickle.pdf2imageconverter.Exception.BadRequestException;
import com.mindtickle.pdf2imageconverter.Exception.FileStorageException;
import com.mindtickle.pdf2imageconverter.model.UploadFileResponse;
import com.mindtickle.pdf2imageconverter.model.JobCreateResponseModel;
import com.mindtickle.pdf2imageconverter.service.FileStorageService;
import com.mindtickle.pdf2imageconverter.service.PDFConverterService;
import com.mindtickle.pdf2imageconverter.utils.Validator;

import lombok.extern.java.Log;

@Log
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class PDF2ImageController {

	@Autowired
	Validator validator;

	@Autowired
	FileStorageService storageService;

	@Autowired
	PDFConverterService pdfConverterService;

	@RequestMapping(value = "/pdf2imageconverter", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> executeConversion(@RequestBody(required = true) @RequestParam("file") MultipartFile file)
			throws BadRequestException, FileStorageException {

		// Here there is no try catch block because whenever this controller class
		// throws an exception it has to be handled separately by
		// RequestExceptionHandler class, Spring boot automatically identifies, that
		// RequestExceptionHandler
		// class
		// is meant for error scenario handling as I have annotated that class with
		// @ControllerAdvice

		// VAlidate if file is of type pdf and file name is in proper format
		validator.isValidFileName(file.getName());

		// I am first uploading the using Pdfupload api so, if we fail uploading it will
		// throw an excedption and will be handled inside RequestExceptionHandler and
		// proper error response will sent to the client
		UploadFileResponse uploadFileResponse = storageService.storeFile(file);

		// Below one is an async method as its a long running job and the server may
		// timeout. so we just invoke it and return the response to user as job
		// submitted successfully
		pdfConverterService.executeConversion(uploadFileResponse.getMediaId(), uploadFileResponse.getNumberOfPages());

		// Assuming mediaId is always unique I am considering that as my JObId which.
		// and I am updating the status of this JObId inside my service.
		JobCreateResponseModel response = JobCreateResponseModel.builder().jobId(uploadFileResponse.getMediaId())
				.message("Job submitted successfully").build();
		log.log(Level.INFO, String.format("Conversion job submitted successfully"));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
