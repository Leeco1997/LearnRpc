package SampleDemo.api;

import SampleDemo.entity.User;

/**
 * @author liqiao
 * @date 2020/7/25 11:39
 * @description
 */

public interface Ihello {

	String sayHello(String name);

	User findUser(String name);
}
