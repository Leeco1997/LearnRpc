package com.rpcdemo01;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author liqiao
 * @date 2020/5/13 15:34
 * @description 使用Bio
 */

public class Server {
    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        ServerSocket socket = new ServerSocket(8888);
        while (true) {
            Socket accept = socket.accept();
            process(accept);
            accept.close();
        }
    }

    private static void process(Socket socket) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();

        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        String clazzName = objectInputStream.readUTF();
        String methodName = objectInputStream.readUTF();
        Class[] parameterTypes = (Class[]) objectInputStream.readObject();
        Object[] args = (Object[]) objectInputStream.readObject();
        //这里可以使用Spring注解
        Class clazz = GoldenEyeImpl.class;
        Method method = clazz.getMethod(methodName, parameterTypes);
        //调用
        Object invoke = method.invoke(clazz.newInstance(), args);

        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
        oos.writeObject(invoke);
        oos.flush();

    }

}
