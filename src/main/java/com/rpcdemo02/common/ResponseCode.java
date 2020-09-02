package com.rpcdemo02.common;

import lombok.Getter;

@Getter
public enum ResponseCode {
    SUCCESS("00000","成功"),
    INVALID_PARAMS("00001", "参数错误"),
    NOMETHOD("00002", "方法不存在"),
    DATA_NULL("0003","数据不存在");

    private String code;
    private String message;
    ResponseCode(String code,String message){
        this.code = code;
        this.message = message;
    }
}
