package com.rpcbynetty.server;

import com.alibaba.fastjson.JSON;
import com.rpcbynetty.common.ResponseCode;
import com.rpcbynetty.common.RpcRequest;
import com.rpcbynetty.common.RpcResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * @author liqiao
 * @date 2020/7/24 14:54
 * @description 如果继承自 SimpleChannelInboundHandler 的话就不要考虑 ByteBuf 的释放 ，{@link SimpleChannelInboundHandler} 内部的
 * channelRead 方法会替你释放 ByteBuf ，避免可能导致的内存泄露问题。
 */
@Slf4j
public class ProcessRequestHandler extends SimpleChannelInboundHandler<String> {
    /**
     * 服务映射
     */
    private Map<String, Object> handlerMap;

    ProcessRequestHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String string) throws Exception {
        log.info("服务端接收到 request{}", string);
        Object result = this.invoke(JSON.parseObject(string, RpcRequest.class));
        //写出返回结果并且关闭channel
        ChannelFuture channelFuture = channelHandlerContext.writeAndFlush(JSON.toJSONString(result));
        channelFuture.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Unexpected exception from downstream.", cause);
        ctx.close();
    }

    private Object invoke(RpcRequest rpcRequest) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String serviceName = rpcRequest.getClassName();
        String methodName = rpcRequest.getMethod();
        String version = rpcRequest.getVersion();
        Object[] params = rpcRequest.getParams();

        Class<?>[] args = Arrays.stream(params).map(Object::getClass).toArray(Class<?>[]::new);
        if (version != null && !"".equals(version)) {
            serviceName = serviceName + "_" + version;
        }
        //其实服务名称默认就是类名+版本号（如果版本号不为空）
        Object service = handlerMap.get(serviceName);
        if (null == service) {
            return RpcResponse.fail(ResponseCode.NOMETHOD);
        }
        Method method = service.getClass().getMethod(methodName, args);
        if (null == method) {
            return RpcResponse.fail(ResponseCode.NOMETHOD);
        }
        return RpcResponse.success(method.invoke(service, params));
    }
}
