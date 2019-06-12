package com.mindtickle.pdf2imageconverter.model;


import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;
import net.minidev.json.annotate.JsonIgnore;

@Setter
@Getter
public class Error {

	@JsonIgnore
	private HttpStatus status;

	private int code;
	private String message;

}
