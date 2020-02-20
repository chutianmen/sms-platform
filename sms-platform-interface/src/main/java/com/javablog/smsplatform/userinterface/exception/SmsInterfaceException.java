package com.javablog.smsplatform.userinterface.exception;

public class SmsInterfaceException extends RuntimeException {
    protected String code;
    protected String msg;

    public SmsInterfaceException() {
    }

    public SmsInterfaceException(String code, String message) {
        super(message);
        this.code = code;
        this.msg = message;
    }

    public SmsInterfaceException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
