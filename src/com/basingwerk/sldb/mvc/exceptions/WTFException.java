package com.basingwerk.sldb.mvc.exceptions;

import org.apache.log4j.Logger;

public class WTFException extends Exception {
    final static Logger logger = Logger.getLogger(WTFException.class);

    public WTFException(String message) {
        super(message);
    }

    public WTFException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
