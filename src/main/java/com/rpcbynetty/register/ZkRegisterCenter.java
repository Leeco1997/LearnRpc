package com.rpcbynetty.register;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

/**
 * @author liqiao
 * @date 2020/7/25 11:38
 * @description 使用curator的zk客户端框架，根据服务名称和服务地址创建临时结点。
 * 一个服务名称可能具有多个服务地址。
 */
@Slf4j
public class ZkRegisterCenter implements ZkRegister {
    public static final String ZK_REGISTER_PATH = "/rpc";

    private String connectionAddress;

    private CuratorFramework curatorFramework;

    public ZkRegisterCenter(String connectionAddress) {
        this.connectionAddress = connectionAddress;
        //初始化curator
        curatorFramework = CuratorFrameworkFactory.builder().connectString(connectionAddress).sessionTimeoutMs(15000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();
        curatorFramework.start();
    }

    @Override
    public void register(String serviceName, String serviceAddress) throws Exception {
        //需要注册的服务根节点
        String servicePath = ZK_REGISTER_PATH + "/" + serviceName;
        //注册服务，创建临时节点
        String serviceAddr = servicePath + "/" + serviceAddress;
        String nodePath = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                .forPath(serviceAddr, "".getBytes());
        log.debug("节点创建成功，节点为:{}", nodePath);
    }
}
