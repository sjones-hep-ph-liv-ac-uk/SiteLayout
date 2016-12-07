package com.basingwerk.sldb.mvc.model;

public class ModelException extends Exception {
	private static final long serialVersionUID = 1L;

	public ModelException(String message) {
		super(message);
	}

	public ModelException(String message, Throwable throwable) {
		super(message, throwable);
	}
}