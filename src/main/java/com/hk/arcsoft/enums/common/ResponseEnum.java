package com.hk.arcsoft.enums.common;

/**
 * Created by HomKey on 2019/12/23.
 */
public enum ResponseEnum {
    SUCCESS(200, "SUCCESS"),
    FAIL(400, "FAIL"),
    ERROR(500, "ERROR");

    private Integer code;
    private String message;

    ResponseEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
