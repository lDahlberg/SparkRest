package com.teamtreehouse.courses.exc;

/**
 * Created by lDahlberg on 8/31/2016.
 */
public class DaoException extends Exception {

    private final Exception originalException;

    public DaoException(Exception originalException, String msg){
        super(msg);
        this.originalException = originalException;
    };

}
