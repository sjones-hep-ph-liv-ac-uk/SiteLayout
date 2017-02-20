package com.basingwerk.sldb.mvc.model;

import org.apache.log4j.Logger;

import com.basingwerk.sldb.mvc.controllers.NodeTypeController;

public class AccessObjectException extends Exception {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(NodeTypeController.class);

    public AccessObjectException(String message) {
        super(message);
    }

    public AccessObjectException(String message, Throwable throwable) {
        super(message, throwable);
    }
}