package com.beTheDonor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> resourceNotFound(ResourceNotFoundException ex) {
		ErrorResponse er = new ErrorResponse();
		er.setMessage(ex.getMessage());
		er.setCode(HttpStatus.NOT_FOUND.value());
		return new ResponseEntity<ErrorResponse>(er,HttpStatus.NOT_FOUND);
		
		
	}
	
	@ExceptionHandler(StockNotPresentException.class)
	public ResponseEntity<ErrorResponse> stockNotPresent(StockNotPresentException ex) {
		ErrorResponse er = new ErrorResponse();
		er.setMessage(ex.getMessage());
		er.setCode(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<ErrorResponse>(er,HttpStatus.BAD_REQUEST);
		
		
	}

}
