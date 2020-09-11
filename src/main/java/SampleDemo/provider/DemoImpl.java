package SampleDemo.provider;

import com.rpcbynetty.annotation.RpcService;
import SampleDemo.api.DemoApi;
/**
 * @author liqiao
 * @date 2020/7/25 17:02
 * @description 使用注解
 */
@RpcService(DemoApi.class)
public class DemoImpl implements DemoApi {
    @Override
    public String say(String hello) {
        return hello;
    }

}




