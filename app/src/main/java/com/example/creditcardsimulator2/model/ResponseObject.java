package com.example.creditcardsimulator2.model;

import com.google.gson.annotations.SerializedName;

public class ResponseObject {
    @SerializedName("status")
    protected String status;
    @SerializedName("message")
    protected String message;
    @SerializedName("data")
    protected Object data;

    public ResponseObject() {
    }

    public ResponseObject(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
