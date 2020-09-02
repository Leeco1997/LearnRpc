package com.rpcdemo02.discover;

/**
 * @author liqiao
 * @date 2020/7/25 11:26
 * @description 需要依赖其他服务的结点
 */

public interface DiscoverService {
     /**
      * 根据服务名称，获得服务的地址
      * @param serviceName
      * @return
      */
     String discover(String serviceName);
}
