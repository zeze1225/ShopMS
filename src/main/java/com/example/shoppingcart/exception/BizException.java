package com.example.shoppingcart.exception;

// 自定义业务异常，暂时没用上，先建个类放着
public class BizException extends RuntimeException {

    private int code;

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() { return code; }
}
