package com.rpcdemo01;

import com.rpcdemo01.common.GoldenEyeApi;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * @author liqiao
 * @date 2020/5/13 15:20
 * @description 生成动态代理
 */
@Slf4j
public class Stub {
    public static Object getStub(final Class clazz) throws IOException {
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Socket socket = new Socket("127.0.0.1", 8888);
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                String clazzName = clazz.getName();
                String methodName = method.getName();
                log.info("{}:{}",clazzName,methodName);
                Class<?>[] parameterTypes = method.getParameterTypes();

                oos.writeUTF(clazzName);
                oos.writeUTF(methodName);
                oos.writeObject(parameterTypes);
                oos.writeObject(args);
                oos.flush();

                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Object o = objectInputStream.readObject();

                oos.close();
                socket.close();

                return o;
            }
        };

        Object o = Proxy.newProxyInstance(GoldenEyeApi.class.getClassLoader(), new Class[]{GoldenEyeApi.class}, invocationHandler);
        System.out.println(o.getClass().getName());
        System.out.println(o.getClass().getInterfaces()[0]);
        return o;

    }
}
