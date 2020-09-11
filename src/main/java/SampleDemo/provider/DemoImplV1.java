package SampleDemo.provider;

import com.rpcbynetty.annotation.RpcService;
import SampleDemo.api.DemoApi;

/**
 * @author liqiao
 * @date 2020/7/27 14:55
 * @description
 */

@RpcService(value = DemoApi.class, version = "1.0")
class DemoImplV1 implements DemoApi {
    @Override
    public String say(String hello) {
        return hello + "1.0";
    }
}