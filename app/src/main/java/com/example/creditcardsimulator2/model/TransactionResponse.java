package com.example.creditcardsimulator2.model;

public class TransactionResponse extends ResponseObject{
    public TransactionResponse() {
    }

    public TransactionResponse(String status, String message, Object data) {
        super(status, message, data);
    }
}
