package com.rpcdemo02.register;

/**
 * @author liqiao
 * @date 2020/7/25 11:34
 * @description
 */

public interface ZkRegister {
    void register(String serviceName,String address) throws Exception;
}
