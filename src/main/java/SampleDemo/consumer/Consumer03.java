package SampleDemo.consumer;

import com.rpcbynetty.client.RpcClientProxy;
import com.rpcbynetty.discover.DiscoverService;
import com.rpcbynetty.discover.DiscoverServiceImpl;
import SampleDemo.api.Ihello;
import SampleDemo.entity.User;
import lombok.extern.slf4j.Slf4j;

/**
 * @author liqiao
 * @date 2020/7/27 14:59
 * @description
 */
@Slf4j
public class Consumer03 {
    public static void main(String[] args) {
        //发现服务
        DiscoverService discoverService = new DiscoverServiceImpl("192.168.1.105:2181");
        RpcClientProxy rpcClientProxy = new RpcClientProxy(discoverService);
        Ihello ihello = rpcClientProxy.clientProxy(Ihello.class);
        System.out.println(ihello.sayHello("lisa"));
        //序列化测试
        User user = ihello.findUser("张三");
        log.info("user: {}", user);
    }
}
