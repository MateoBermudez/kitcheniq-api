package com.uni.kitcheniq.exception;

public class ErrorMessage {
    private String message;
    private String cause;
    private int statusCode;
    private long timestamp;
    private String path;
    private String errorCode;

    public ErrorMessage(String message, String cause, int statusCode, long timestamp, String path, String errorCode) {
        this.message = message;
        this.cause = cause;
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.path = path;
        this.errorCode = errorCode;
    }

    public ErrorMessage() {
        // Default constructor
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
