package com.beTheDonor.exception;

public class StockNotPresentException extends RuntimeException {
	private static final long serialVersionUID = 1L;
//	In case the requested quantity through PUT request is more than the quantity available in the DB
	public StockNotPresentException(String message) {
		super(message);
	}

}

