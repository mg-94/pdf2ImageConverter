
package com.mindtickle.pdf2imageconverter.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class JobCreateResponseModel {
	String jobId;
	String message;
}
