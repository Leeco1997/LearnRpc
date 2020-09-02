package com.rpcdemo02.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rpcdemo02.common.ResponseCode;
import com.rpcdemo02.common.RpcRequest;
import com.rpcdemo02.common.RpcResponse;
import com.rpcdemo02.discover.DiscoverService;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author liqiao
 * @date 2020/7/26 11:34
 * @description
 */

@Slf4j
public class RpcInvocationHandler implements InvocationHandler {

	private DiscoverService serverDiscover;

	private String version;

	public RpcInvocationHandler(DiscoverService serverDiscover, String version) {
		this.serverDiscover = serverDiscover;
		this.version = version;
	}


	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

			RpcRequest request = new RpcRequest();
			request.setClassName(method.getDeclaringClass().getName());
			request.setMethod(method.getName());
			request.setParams(args);
			request.setVersion(version);
			String serviceName = method.getDeclaringClass().getName();
			if (null != version && !"".equals(version)) {
				serviceName += "_" + version;
			}
			String servicePath = serverDiscover.discover(serviceName);
			if (null == servicePath) {
				log.error("并未找到服务地址,className:{}", serviceName);
				throw new RuntimeException("未找到服务地址");
			}
			String host = servicePath.split(":")[0];
			int port = Integer.parseInt(servicePath.split(":")[1]);
			RpcResponse response = new NettyTransport(host, port).send(request);
			if (response == null) {
				throw new RuntimeException("调用服务失败,servicePath:" + servicePath);
			}
			if (response.getCode() == null || !response.getCode().equals(ResponseCode.SUCCESS.getCode())) {
				log.error("调用服务失败,servicePath:{},RpcResponse:{}", servicePath,
						JSON.toJSONString(response));
				throw new RuntimeException(response.getMessage());
			} else {
				return response.getData();
			}
	}
}
