package com.rpcdemo02.seriablize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Base64;

/**
 * @author liqiao
 * @date 2020/7/28 13:32
 * @description
 */

public class KyroUtils {
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> String serializationObject(T obj) {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.register(obj.getClass(), new JavaSerializer());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeClassAndObject(output, obj);
        output.flush();
        output.close();

        byte[] b = baos.toByteArray();
        try {
            baos.flush();
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(new Base64().encode(b));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Serializable> T deserializationObject(String obj,
                                                                   Class<T> clazz) {
        Kryo kryo = new Kryo();
        kryo.setReferences(false);
        kryo.register(clazz, new JavaSerializer());

        ByteArrayInputStream bais = new ByteArrayInputStream(
                new Base64().decode(obj));
        Input input = new Input(bais);
        return (T) kryo.readClassAndObject(input);
    }
}
