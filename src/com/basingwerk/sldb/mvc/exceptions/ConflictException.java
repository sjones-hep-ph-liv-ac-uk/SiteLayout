package com.basingwerk.sldb.mvc.exceptions;

import org.apache.log4j.Logger;

public class ConflictException extends Exception {
    final static Logger logger = Logger.getLogger(ConflictException.class);

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
