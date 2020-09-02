package com.rpcdemo02.seriablize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.rpcdemo02.common.RpcRequest;
import com.rpcdemo02.common.RpcResponse;
import lombok.extern.slf4j.Slf4j;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


/**
 * @author liqiao
 * @date 2020/7/27 17:30
 * @description 使用kryo实现序列化
 */
@Slf4j
public class KryoSeriablize implements Serializer {
    final ThreadLocal<Kryo> kryoThreadLocal = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.register(RpcResponse.class);
            kryo.register(RpcRequest.class);
            return kryo;
        }
    };

    @Override
    public byte[] seriablize(Object object) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Output output = new Output(byteArrayOutputStream);
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, object);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (KryoException e) {
            throw new RuntimeException("反序列化异常");
        }

    }

    @Override
    public <T> T deSerialize(byte[] bytes, Class<T> clazz) {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            Input input = new Input(byteArrayInputStream);
            Kryo kryo = kryoThreadLocal.get();
            Object o = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return clazz.cast(o);
        } catch (KryoException e) {
            throw new RuntimeException("反序列化异常");
        }
    }


}
