package com.teamtreehouse.testing;

/**
 * Created by lDahlberg on 9/2/2016.
 */
public class ApiResponse {

    private final int status;
    private final String body;

    public ApiResponse(int status, String body ) {
        this.body = body;
        this.status = status;
    }

    public String getBody() {
        return body;
    }

    public int getStatus() {
        return status;
    }
}
