package com.rpcbynetty.client;

import com.alibaba.fastjson.JSON;
import com.rpcbynetty.common.RpcRequest;
import com.rpcbynetty.common.RpcResponse;
import com.rpcbynetty.seriablize.KryoSeriablize;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author liqiao
 * @date 2020/7/25 11:34
 * @description
 */

@Slf4j
public class NettyTransport {

    private static Bootstrap bootstrap;

    private String host;

    private int port;
    private static KryoSeriablize kryoSeriablize;

    public NettyTransport(String host, int port) {
        this.host = host;
        this.port = port;
    }

    static {
        bootstrap = new Bootstrap();
        kryoSeriablize = new KryoSeriablize();
        EventLoopGroup group = new NioEventLoopGroup();

        bootstrap.group(group).channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000);
        bootstrap.handler(new ChannelInitializer<Channel>() {

            @Override
            protected void initChannel(Channel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                //心跳机制
                pipeline.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                pipeline.addLast(new LengthFieldPrepender(4));
                //编码解码
//                pipeline.addLast(new NettyKryoDecoder(kryoSeriablize, RpcResponse.class));
//                pipeline.addLast(new NettyKryoEncoder(kryoSeriablize, RpcRequest.class));
                // 解码 bytes=>resp
                pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                pipeline.addLast(new ClientHandler());
            }
        });
    }

    public RpcResponse send(RpcRequest request) throws InterruptedException {
        ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
        Channel channel = channelFuture.channel();
        log.info("发送请求{}", request);
        channel.writeAndFlush(request);
        //当通道关闭了，就继续往下走,阻塞等待
        // TODO: 2020/9/11 可以使用 CompletableFuture 进行优化 
        channelFuture.channel().closeFuture().sync();
        AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
        return channel.attr(key).get();
    }

    public static class ClientHandler extends SimpleChannelInboundHandler<String> {

        @Override
        protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
            log.debug("收到response:{}", s);
            RpcResponse response = JSON.parseObject(s, RpcResponse.class);
            AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
            //把结果保存在attr
            channelHandlerContext.channel().attr(key).set(response);
            channelHandlerContext.channel().close();

        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            ChannelFuture channelFuture = ctx.writeAndFlush(Unpooled.copiedBuffer("connect success".getBytes()));
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    log.info("send message success");
                }
            });
            super.channelActive(ctx);
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            log.error("Unexpected exception from upstream.", cause);
            super.exceptionCaught(ctx, cause);
        }
    }

}
