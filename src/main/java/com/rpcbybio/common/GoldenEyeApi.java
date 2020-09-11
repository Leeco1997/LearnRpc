package com.rpcbybio.common;

/**
 * @author liqiao
 * @date 2020/5/13 14:57
 * @description
 */

public interface GoldenEyeApi  {
    User findById(Integer id);

    void save(User user);
}
