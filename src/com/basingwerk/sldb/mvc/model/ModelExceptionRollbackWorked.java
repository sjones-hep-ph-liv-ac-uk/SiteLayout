package com.basingwerk.sldb.mvc.model;

public class ModelExceptionRollbackWorked extends ModelException {
    private static final long serialVersionUID = 1L;

    public ModelExceptionRollbackWorked(String message, Throwable throwable) {
        super(message, throwable);
    }

}
