package com.rpcbynetty.discover.loadbalance;

import java.util.List;

/**
 * @author liqiao
 * @date 2020/7/25 11:23
 * @description
 */
public abstract class AbstractLoadBalance implements LoadBalance {

	@Override
	public String selectServiceAddress(List<String> serviceAddresses) {
		if (serviceAddresses == null || serviceAddresses.size() == 0) {
			return null;
		}
		if (serviceAddresses.size() == 1) {
			return serviceAddresses.get(0);
		}
		return doSelect(serviceAddresses);
	}

	protected abstract String doSelect(List<String> serviceAddresses);
}
