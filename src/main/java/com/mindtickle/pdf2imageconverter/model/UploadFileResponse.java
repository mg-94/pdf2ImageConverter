package com.mindtickle.pdf2imageconverter.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UploadFileResponse {
	String mediaId;
	int NumberOfPages;
	// If the contract for fileUploadServie changes we have make this class in
	// sync the contract of fileUploadservice.
}
