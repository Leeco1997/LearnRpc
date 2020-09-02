package demo.provider;

import com.rpcdemo02.annotation.RpcService;
import demo.api.DemoApi;

/**
 * @author liqiao
 * @date 2020/7/27 14:56
 * @description
 */


@RpcService(value = DemoApi.class, version = "3.0")
class DemoImplV3 implements DemoApi {
    @Override
    public String say(String hello) {
        return hello + "3.0";
    }
}
