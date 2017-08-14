package com.basingwerk.sldb.mvc.exceptions;

import org.apache.log4j.Logger;

public class RoutineException extends Exception {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(RoutineException.class);

    public RoutineException(String message) {
        super(message);
    }

    public RoutineException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
