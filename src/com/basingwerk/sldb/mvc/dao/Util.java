package com.basingwerk.sldb.mvc.dao;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;

import com.basingwerk.sldb.mvc.exceptions.RoutineException;
import com.basingwerk.sldb.mvc.exceptions.WTFException;

public class Util {
    final static Logger logger = Logger.getLogger(Util.class);
    
    

    public Util() {
    }

    public static boolean isIdClash(Exception e) throws RoutineException, WTFException{
        Throwable t = e.getCause();
        while ((t != null) && !(t instanceof ConstraintViolationException)) {
            t = t.getCause();
        }
        if (t instanceof ConstraintViolationException) {
            return true;
        }
        return false;
    }
}
