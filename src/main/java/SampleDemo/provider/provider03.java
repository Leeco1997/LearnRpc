package SampleDemo.provider;

import com.rpcbynetty.register.ZkRegister;
import com.rpcbynetty.register.ZkRegisterCenter;
import com.rpcbynetty.server.RpcServer;
import SampleDemo.api.Ihello;

import java.util.Arrays;

/**
 * @author liqiao
 * @date 2020/7/27 14:58
 * @description
 */

public class provider03
{
    public static void main(String[] args) {
        Ihello hello = new HelloImpl();
        ZkRegister zkRegister = new ZkRegisterCenter("192.168.1.105:2181");
        RpcServer rpcServer = new RpcServer(zkRegister, "127.0.0.1", 9991);
        rpcServer.bindService(Arrays.asList(hello));
        try {
            rpcServer.publish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
