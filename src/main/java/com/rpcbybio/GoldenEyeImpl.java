package com.rpcbybio;

import com.rpcbybio.common.GoldenEyeApi;
import com.rpcbybio.common.User;
import lombok.extern.slf4j.Slf4j;

/**
 * @author liqiao
 * @date 2020/5/13 15:02
 * @description
 */
@Slf4j
public class GoldenEyeImpl implements GoldenEyeApi {
    @Override
    public User findById(Integer id) {
        log.info("{}",id);
        return new User(01, "李华", 20);
    }

    @Override
    public void save(User user) {

    }
}
