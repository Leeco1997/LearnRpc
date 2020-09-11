package com.rpcbybio;

import com.rpcbybio.common.GoldenEyeApi;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author liqiao
 * @date 2020/5/13 14:56
 * @description
 */
@Slf4j
public class Client {
    public static void main(String[] args) throws IOException {
        GoldenEyeApi stub =(GoldenEyeApi) Stub.getStub(GoldenEyeApi.class);
        //User(id=1, name=李华, age=20)
        log.info("user={}",stub.findById(1));
    }
}
