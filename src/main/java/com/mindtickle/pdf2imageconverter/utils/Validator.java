package com.mindtickle.pdf2imageconverter.utils;

import org.springframework.stereotype.Component;

import com.mindtickle.pdf2imageconverter.Exception.BadRequestException;

@Component
public class Validator  {
		public boolean isValidFileName(String fileName) throws BadRequestException {
			if(fileName.contains("..")) {
                throw new BadRequestException("Sorry! Filename contains invalid path sequence " + fileName);
            }
			
			if(!fileName.split(".")[1].equals("pdf")) {
				throw new BadRequestException("File format not supported" + fileName);
			}
			
			return true;
		}
}
