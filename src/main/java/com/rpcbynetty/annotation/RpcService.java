package com.rpcbynetty.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liqiao
 * @date 2020/7/24 11:33
 * @description 自定义注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcService {
    /**
     * 接口地址
     */
    Class<?> value();

    /**
     * 版本号
     *
     * @return
     */
    String version() default "";
}
