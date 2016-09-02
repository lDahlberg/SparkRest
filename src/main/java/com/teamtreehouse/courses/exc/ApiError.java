package com.teamtreehouse.courses.exc;

/**
 * Created by lDahlberg on 9/2/2016.
 */
public class ApiError extends RuntimeException {

    private final int status;

    public ApiError(int status, String msg) {
        super(msg);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
