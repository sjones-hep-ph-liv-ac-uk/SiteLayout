package com.basingwerk.sldb.mvc.model;

import org.apache.log4j.Logger;

import com.basingwerk.sldb.mvc.controllers.NodeTypeController;

public class ModelException extends Exception {
	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(NodeTypeController.class);

	public ModelException(String message) {
		super(message);
	}

	public ModelException(String message, Throwable throwable) {
		super(message, throwable);
	}
}