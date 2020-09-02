package com.rpcdemo02.client;

import com.rpcdemo02.discover.DiscoverService;

import java.lang.reflect.Proxy;

/**
 * @author liqiao
 * @date 2020/7/26 11:34
 * @description
 */

public class RpcClientProxy {

	private DiscoverService serverDiscover;

	public RpcClientProxy(DiscoverService serverDiscover) {
		this.serverDiscover = serverDiscover;
	}

	/**
	 * 基于服务接口和版本创建代理
	 *
	 * @param interfaceCls 服务接口
	 * @param version      版本
	 * @param <T>          泛型
	 *
	 * @return 实现该节点的代理对象
	 */
	public <T> T clientProxy(Class<T> interfaceCls, String version) {
		return (T) Proxy.newProxyInstance(interfaceCls.getClassLoader(), new Class[] { interfaceCls },
				new RpcInvocationHandler(serverDiscover, version));
	}

	public <T> T clientProxy(Class<T> interfaceCls) {
		return this.clientProxy(interfaceCls, null);
	}
}
