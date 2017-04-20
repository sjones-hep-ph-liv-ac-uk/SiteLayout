package com.basingwerk.sldb.mvc.model;

public class ModelExceptionRollbackFailed extends ModelException {
    private static final long serialVersionUID = 1L;

    public ModelExceptionRollbackFailed(String message, Throwable throwable) {
        super(message, throwable);
    }

}