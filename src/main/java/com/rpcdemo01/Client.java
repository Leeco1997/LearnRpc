package com.rpcdemo01;

import com.rpcdemo01.common.GoldenEyeApi;

import java.io.IOException;

/**
 * @author liqiao
 * @date 2020/5/13 14:56
 * @description
 */

public class Client {
    public static void main(String[] args) throws IOException {
        GoldenEyeApi stub =(GoldenEyeApi) Stub.getStub(GoldenEyeApi.class);
        //User(id=1, name=李华, age=20)
        System.out.println(stub.findById(1));
    }
}
