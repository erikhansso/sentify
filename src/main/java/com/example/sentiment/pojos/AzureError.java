package com.example.sentiment.pojos;

public class AzureError {

    private String id;
    private String message;

    public AzureError() {
    }

    public AzureError(String id, String message) {
        this.id = id;
        this.message = message;
    }

    @Override
    public String toString() {
        return "AzureError{" +
                "id='" + id + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
