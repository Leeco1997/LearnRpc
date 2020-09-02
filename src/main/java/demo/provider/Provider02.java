package demo.provider;

import com.rpcdemo02.register.ZkRegister;
import com.rpcdemo02.register.ZkRegisterCenter;
import com.rpcdemo02.server.RpcServer;
import demo.api.DemoApi;

import java.util.Arrays;

/**
 * @author liqiao
 * @date 2020/7/27 10:12
 * @description
 */

public class Provider02 {
    public static void main(String[] args) {
        DemoApi demoService3 = new DemoImplV3();
        ZkRegister zkRegister = new ZkRegisterCenter("192.168.1.105:2181");
        RpcServer rpcServer = new RpcServer(zkRegister, "127.0.0.1", 9999);
        rpcServer.bindService(Arrays.asList(demoService3));
        try {
            rpcServer.publish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
