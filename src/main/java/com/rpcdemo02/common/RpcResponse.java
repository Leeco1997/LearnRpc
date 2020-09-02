package com.rpcdemo02.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author liqiao
 * @date 2020/7/24 15:04
 * @description
 */
@Data
public class RpcResponse<T> implements Serializable {
    private static final long serialVersionUID = -6365407285028625978L;
    private String code;
    private String message;
    private T data;


    public static <T> RpcResponse<T> success(T data) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(ResponseCode.SUCCESS.getCode());
        response.setMessage(ResponseCode.SUCCESS.getMessage());
        if (data != null) {
            response.setData(data);
        }
        return response;
    }

    /**
     * 失败响应
     *
     * @param responseCode 响应码枚举
     * @param <T>          泛型
     * @return RpcResponse
     */
    public static <T> RpcResponse<T> fail(ResponseCode responseCode) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(responseCode.getCode());
        response.setMessage(response.getMessage());
        return response;
    }
}
