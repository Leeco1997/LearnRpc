package com.rpcbynetty.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liqiao
 * @date 2020/7/24 15:00
 * @description 封装请求
 */
@Data
public class RpcRequest implements Serializable {
    private static final long serialVersionUID = 8525605060027770024L;

    private String className;

    private String method;

    private String version;

    private Object[] params;

}
