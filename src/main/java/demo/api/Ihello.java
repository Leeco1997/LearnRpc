package demo.api;

import demo.entity.User;

/**
 * @author liqiao
 * @date 2020/7/25 11:39
 * @description
 */

public interface Ihello {

	String sayHello(String name);

	User findUser(String name);
}
