package com.rpcbynetty.seriablize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.rpcbynetty.common.RpcRequest;
import com.rpcbynetty.common.RpcResponse;
import com.rpcbynetty.common.exception.SerializeException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author liqiao
 * @date 2020/7/28 13:32
 * @description
 */

@Slf4j
class KryoSerializer implements Serializer {

    /**
     * 由于 Kryo 不是线程安全的。每个线程都应该有自己的 Kryo，Input 和 Output 实例。
     * 所以，使用 ThreadLocal 存放 Kryo 对象
     */
    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(RpcResponse.class);
        kryo.register(RpcRequest.class);
        kryo.setReferences(true); //默认值为true,是否关闭注册行为,关闭之后可能存在序列化问题，一般推荐设置为 true
        kryo.setRegistrationRequired(false); //默认值为false,是否关闭循环引用，可以提高性能，但是一般不推荐设置为 true
        return kryo;
    });


    @Override
    public byte[] seriablize(Object object) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            // Object->byte:将对象序列化为byte数组
            kryo.writeObject(output, object);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (Exception e) {
            throw new SerializeException("seriablize failed");
        }
    }

    @Override
    public <T> T deSerialize(byte[] bytes, Class<T> clazz) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            // byte->Object:从byte数组中反序列化出对对象
            Object o = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return clazz.cast(o);
        } catch (Exception e) {
            throw new SerializeException("deSerialize failed");
        }
    }
}
