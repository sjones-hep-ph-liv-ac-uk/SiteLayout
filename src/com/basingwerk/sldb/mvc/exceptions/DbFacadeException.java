package com.basingwerk.sldb.mvc.exceptions;

import org.apache.log4j.Logger;

public class DbFacadeException extends Exception {
    private static final long serialVersionUID = 1L;
    final static Logger logger = Logger.getLogger(DbFacadeException.class);

    public DbFacadeException(String message) {
        super(message);
    }

    public DbFacadeException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
