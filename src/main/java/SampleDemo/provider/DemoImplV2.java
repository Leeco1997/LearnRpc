package SampleDemo.provider;

import com.rpcbynetty.annotation.RpcService;
import SampleDemo.api.DemoApi;

/**
 * @author liqiao
 * @date 2020/7/27 14:55
 * @description
 */

@RpcService(value = DemoApi.class, version = "2.0")
class DemoImplV2 implements DemoApi {
    @Override
    public String say(String hello) {
        return hello + "2.0";
    }
}