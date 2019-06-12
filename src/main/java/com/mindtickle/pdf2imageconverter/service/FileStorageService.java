package com.mindtickle.pdf2imageconverter.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.mindtickle.pdf2imageconverter.Exception.FileStorageException;
import com.mindtickle.pdf2imageconverter.model.FileResponseBody;
import com.mindtickle.pdf2imageconverter.model.Status;
import com.mindtickle.pdf2imageconverter.model.UploadFileResponse;

import lombok.extern.java.Log;

@Service
@Log
public class FileStorageService {

	@Autowired
	private RestTemplate restTemplate;

	public UploadFileResponse storeFile(MultipartFile file) throws FileStorageException {
		String fileName = file.getOriginalFilename();
		ResponseEntity<UploadFileResponse> responseBody = null;
		try {

			// Here we call the UploadPDFFile api which is available to us to store the pdf
			// to our local server and it returns us mediaID and number of Pages in
			// response.

			responseBody = restTemplate.postForEntity("URL to UPLoAD pDF file API", populateHeaders(),
					UploadFileResponse.class);

		} catch (RestClientException | GeneralSecurityException ex) {
			log.log(Level.SEVERE, "Error uploading file: " + fileName);
			throw new FileStorageException("Could not store file " + fileName + ". Please try again after sometime!",
					ex);
		}
		return responseBody.getBody();

	}

	public void updateJobStatus(Status status, String jobId) throws FileStorageException {
		try {
			// Write the logic to update the status of the given jobID in our database
			// This status can be used to poll the status of our running job.

		} catch (IOException e) {
			log.log(Level.SEVERE, "Failed updaing status of JobId: " + jobId);
			throw new FileStorageException("Failed updaing status of JobId: " + jobId);
		}
	}

	public HttpEntity<Object> populateHeaders() throws GeneralSecurityException {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		return new HttpEntity<>(headers);
	}

	public void writeBatchFiles(List<FileResponseBody> imageFileList) throws FileStorageException {
		try {
			// Write the logic to upload the image file to our database with primary key as
			// FileResponseBody.getFileId();

		} catch (IOException e) {
			log.log(Level.SEVERE, "Failed writing images to database");
			throw new FileStorageException("Failed writing images to database");
		}

	}

	public void writeFailedPageNumbers(String mediaId, int pageNo) throws FileStorageException {
		try {
			// Write the logic to write the pageNumber of a given mediaId to database

		} catch (IOException e) {
			log.log(Level.SEVERE, "Error writing page number of failed pages to database for media Id: "
					+ mediaId + " and pageNo.: " + pageNo);
			throw new FileStorageException("Error writing page number of failed pages to database for media Id: "
					+ mediaId + " and pageNo.: " + pageNo);
		}
	}

}
