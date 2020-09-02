package com.rpcdemo02.discover;

import com.alibaba.fastjson.JSON;
import com.rpcdemo02.discover.loadbalance.LoadBalance;
import com.rpcdemo02.discover.loadbalance.RandomLoadBalance;
import com.rpcdemo02.register.ZkRegisterCenter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.KeeperException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liqiao
 * @date 2020/7/25 16:01
 * @description 使用zk完成服务发现，维护一个服务名称和服务地址的本地map，如果map中不存在，则遍历所有的子节点。
 * 每次更新子节点，都需要更新map。
 * 类似于客户端和注册中心的 发布-> 订阅通知。
 */
@Slf4j
public class DiscoverServiceImpl implements DiscoverService {

    Map<String, List<String>> serviceAddressMap = new ConcurrentHashMap<>();

    private String connectionAddress;

    private LoadBalance loadBalance;

    private CuratorFramework curatorFramework;

    public DiscoverServiceImpl(String connectionAddress) {
        this.connectionAddress = connectionAddress;
        this.loadBalance = new RandomLoadBalance();
        curatorFramework = CuratorFrameworkFactory.builder().
                connectString(connectionAddress).
                sessionTimeoutMs(15000).
                retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();
        curatorFramework.start();
    }


    @Override
    public String discover(String serviceName) {
        List<String> serviceAddressList;
        //如果本地map中没有
        if (!serviceAddressMap.containsKey(serviceName)) {
            String path = ZkRegisterCenter.ZK_REGISTER_PATH + '/' + serviceName;
            try {
                serviceAddressList = curatorFramework.getChildren().forPath(path);
                serviceAddressMap.put(serviceName, serviceAddressList);
                //监听
                watcher(serviceName);
            } catch (Exception e) {
                if (e instanceof KeeperException.NoNodeException) {
                    log.info("no such node,serviceName:{}", serviceName);
                    serviceAddressList = null;
                } else {
                    throw new RuntimeException("get node failed: " + e);
                }
            }
        }
        //查询本地map
        else {
            serviceAddressList = serviceAddressMap.get(serviceName);
        }
        return loadBalance.selectServiceAddress(serviceAddressList);
    }

    private void watcher(String serviceName) {
        String path = ZkRegisterCenter.ZK_REGISTER_PATH + '/' + serviceName;
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, path, true);
        PathChildrenCacheListener pathChildrenCacheListener = (curatorFramework, pathChildrenCacheEvent) -> {
            List<String> strings = curatorFramework.getChildren().forPath(path);
            serviceAddressMap.put(serviceName, strings);
        };
        pathChildrenCache.getListenable().addListener(pathChildrenCacheListener);
        try {
            pathChildrenCache.start();
        } catch (Exception e) {
            throw new RuntimeException("children watch has failed, {}", e);
        }
    }
}
