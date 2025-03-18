package com.example.exception;

public class CustomerException extends RuntimeException {
    private String code;
    private String msg;

    public CustomerException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CustomerException(String msg) {
        this.code = "500";
        this.msg = msg;
    }

    public CustomerException() {}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
