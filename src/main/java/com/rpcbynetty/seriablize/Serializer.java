package com.rpcbynetty.seriablize;

/**
 * @author liqiao
 * @date 2020/7/27 17:30
 * @description
 */

public interface Serializer {
    byte[] seriablize(Object object);

    <T> T deSerialize(byte[] bytes, Class<T> clazz);
}
