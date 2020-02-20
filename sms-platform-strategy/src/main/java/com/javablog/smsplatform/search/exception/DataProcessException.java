package com.javablog.smsplatform.search.exception;


public class DataProcessException extends Exception {

    private String errorCode;

    public DataProcessException(String errorCode) {
        this.errorCode = errorCode;
    }

    public DataProcessException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public DataProcessException(String message, Throwable cause, String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public DataProcessException(Throwable cause, String errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
