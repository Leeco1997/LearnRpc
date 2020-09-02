package demo.provider;

import com.rpcdemo02.register.ZkRegister;
import com.rpcdemo02.register.ZkRegisterCenter;
import com.rpcdemo02.server.RpcServer;
import demo.api.DemoApi;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @author liqiao
 * @date 2020/7/25 17:03
 * @description
 */
@Slf4j
public class Provider01 {
    public static void main(String[] args) {
        DemoApi demoService1 = new DemoImpl();
        DemoApi demoService2 = new DemoImplV1();
        DemoApi demoService3 = new DemoImplV2();
        ZkRegister zkRegister = new ZkRegisterCenter("192.168.1.105:2181");
        RpcServer rpcServer = new RpcServer(zkRegister, "127.0.0.1", 8888);
        rpcServer.bindService(Arrays.asList(demoService1, demoService2, demoService3));
        try {
            rpcServer.publish();
        } catch (InterruptedException e) {
            log.info("register failed {}", e.toString());
        }


    }
}
