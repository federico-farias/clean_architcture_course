package com.example.demo.users.porin;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class MicronoutHttpErrorResponse extends MicronoutHttpResponse {

    private String message;

    public MicronoutHttpErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
