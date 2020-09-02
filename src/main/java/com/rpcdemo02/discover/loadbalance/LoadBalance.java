package com.rpcdemo02.discover.loadbalance;

import java.util.List;

/**
 * @author liqiao
 * @date 2020/7/25 11:20
 * @description
 */
public interface LoadBalance {

	/**
	 * 在已有服务列表中选择一个服务路径
	 * @param serviceAddresses 服务地址列表
	 *
	 * @return 服务地址
	 */
	String selectServiceAddress(List<String> serviceAddresses);

}
