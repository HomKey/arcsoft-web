package com.hk.arcsoft.dto.common;


import com.hk.arcsoft.enums.common.ResponseEnum;

/**
 * Created by HomKey on 2019/12/23.
 */
public class ResponseResult {
    private Integer code;
    private String message;
    private Object data;

    public ResponseResult(ResponseEnum responseEnum, Object data) {
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
        this.data = data;
    }

    public ResponseResult(ResponseEnum responseEnum) {
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
