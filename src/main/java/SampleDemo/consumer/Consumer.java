package SampleDemo.consumer;

import com.rpcbynetty.client.RpcClientProxy;
import com.rpcbynetty.discover.DiscoverService;
import com.rpcbynetty.discover.DiscoverServiceImpl;
import SampleDemo.api.DemoApi;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author liqiao
 * @date 2020/7/25 17:00
 * @description
 */
@Slf4j
public class Consumer {
    public static void main(String[] args) throws InterruptedException {
        DiscoverService discoverService = new DiscoverServiceImpl("192.168.1.105:2181");
        RpcClientProxy rpcClientProxy = new RpcClientProxy(discoverService);
        try {
            DemoApi demoApi = rpcClientProxy.clientProxy(DemoApi.class, "1.0");
            // java.lang.ArrayIndexOutOfBoundsException:
            System.out.println( demoApi.say("consumer-01"));
        } catch (Exception e) {
            log.info("consumer remote invoke failed {}", e.toString());
        }

        //测试负载
        for (int i = 0; i < 20; i++) {
            DemoApi demoApi = rpcClientProxy.clientProxy(DemoApi.class);
            try {
                System.out.println(demoApi.say("loadBalance consumer test"));
            } catch (Exception e) {
                log.info("load balance remote invoke failed {}", e.toString());
            }
        }

        //序列化测试

        TimeUnit.SECONDS.sleep(2);
    }
}
