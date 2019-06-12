package com.mindtickle.pdf2imageconverter.service;

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.mindtickle.pdf2imageconverter.model.FileResponseBody;
import com.mindtickle.pdf2imageconverter.model.JobCreateResponseModel;
import com.mindtickle.pdf2imageconverter.model.Status;

import lombok.extern.java.Log;

@Component
@Log
public class PDFConverterService {
	// Currently I am hardcoding this batch_size but its a good practice to
	// externalize this kind of attributes, as they may change on changing the
	// infrastructure
	private static final int BATCH_SIZE = 100;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	FileStorageService storageService;

	@Async
	public JobCreateResponseModel executeConversion(String mediaId, int numberOfPages) {

		try {
			storageService.updateJobStatus(Status.PROCESSING, mediaId);
			int startPageNumber = 0;
			int endPageNumber = 0;

			while (endPageNumber < numberOfPages) {
				startPageNumber = endPageNumber + 1;
				endPageNumber = Math.min(startPageNumber + BATCH_SIZE, numberOfPages);
				fetchImageInBatch(mediaId, startPageNumber, endPageNumber);

			}

			storageService.updateJobStatus(Status.SUCCESS, mediaId);
			// Send an email /Web notification to the client that job completed
			// successfully.

		} catch (Exception e) {

			log.log(Level.SEVERE,
					String.format("%s: Failed transforming all the pages to image of mediaId %s", mediaId));
			// Send an email /Web notification to the client that job completed Failed.
			storageService.updateJobStatus(Status.FAILED, mediaId);

		}
	}

	private void fetchImageInBatch(String mediaId, int lowerPageNumber, int higherPageNumber) {

		IntStream stream = IntStream.range(lowerPageNumber, higherPageNumber);
		// Make parallel calls to fetchImage for pageNumber api
		List<FileResponseBody> imageFileList = stream.parallel().map(index -> fetchImage(index, mediaId))
				.collect(Collectors.toList());

		// Once we have fetched the images in batches write them down to the db in batch
		// We are doing this batching because writing to dB for every single page is
		// expensive

		storageService.writeBatchFiles(imageFileList);
		// No try Catch for above method call because if we failed writing the processed
		// batch files to our db
		// then we may reach in inconsistency so its better to stop the process here

	}

	protected FileResponseBody fetchImage(int pageNo, String mediaId) {

		int retryCnt = 0;
		while (retryCnt < 3) {
			try {
				ResponseEntity<FileResponseBody> responseBody = restTemplate
						.postForEntity("URL to fetch image from media API", populateHeaders(), FileResponseBody.class);
				return responseBody.getBody();
			} catch (RestClientException e) {
				retryCnt++;
			}
		}

		// If we have not returned even after trying 3 times log it and move ahead
		log.log(Level.SEVERE,
				String.format("%s: Failed fetching image file for page no %n of mediaId %s", pageNo, mediaId));

		storageService.writeFailedPageNumbers(mediaId, pageNo);
		// No try catch for the above method call as if it fails to write the page
		// number of failed file we should stop the process otherwise we will enter an
		// inconsistent state. When some is retrying with the same file
	}

	public HttpEntity<Object> populateHeaders() throws GeneralSecurityException {
		MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		return new HttpEntity<>(headers);
	}
}
