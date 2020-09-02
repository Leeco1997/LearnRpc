package demo.provider;

import com.rpcdemo02.annotation.RpcService;
import demo.api.Ihello;
import demo.entity.User;
import lombok.extern.slf4j.Slf4j;

/**
 * @author liqiao
 * @date 2020/7/27 14:56
 * @description
 */
@Slf4j
@RpcService(Ihello.class)
public class HelloImpl implements Ihello {
    @Override
    public String sayHello(String name) {
        return "HelloImpl.sayHello:" + name;
    }

    @Override
    public User findUser(String name) {
        log.info("成功调用接口{}", name);
        return User.builder().name("张三").age(18).build();
    }
}
