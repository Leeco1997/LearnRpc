package com.rpcbynetty.discover.loadbalance;

import java.util.List;
import java.util.Random;

/**
 * @author liqiao
 * @date 2020/7/25 13:26
 * @description
 */
public class RandomLoadBalance extends AbstractLoadBalance {

	@Override
	protected String doSelect(List<String> serviceAddresses) {
		Random random = new Random();
		return serviceAddresses.get(random.nextInt(serviceAddresses.size()));
	}
}
