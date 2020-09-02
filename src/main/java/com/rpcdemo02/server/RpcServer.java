package com.rpcdemo02.server;

import com.rpcdemo02.annotation.RpcService;
import com.rpcdemo02.register.ZkRegister;
import com.rpcdemo02.seriablize.KryoSeriablize;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author liqiao
 * @date 2020/7/24 11:25
 * @description 远程服务端的服务启动和绑定
 */

@Slf4j
public class RpcServer {
    /**
     * 注册中心
     */
    private ZkRegister zkRegister;
    /**
     * 服务发布的ip
     */
    private String serviceIp;
    /**
     * 服务发布的端口号
     */
    private Integer servicePort;
    /**
     * 服务名称和服务对象的映射
     */
    private Map<String, Object> handlerMap = new HashMap<>();
    private KryoSeriablize kryoSeriablize = new KryoSeriablize();

    public RpcServer(ZkRegister zkRegister, String serviceIp, Integer servicePort) {
        this.zkRegister = zkRegister;
        this.serviceIp = serviceIp;
        this.servicePort = servicePort;
    }

    /**
     * 绑定服务名称和服务版本号
     *
     * @param services
     */
    public void bindService(List<Object> services) {
        for (Object service : services) {
            //根据注解获得服务名称和服务版本号
            RpcService annotation = service.getClass().getAnnotation(RpcService.class);
            if (annotation == null) {
                throw new RuntimeException("The Service doesn't annotate" + service.getClass());
            }
            String serviceName = annotation.value().getName();
            String serviceVersion = annotation.version();
            if (!serviceVersion.equals("")) {
                serviceName += "_" + serviceVersion;
            }
            //服务名称和服务版本号
            handlerMap.put(serviceName, service);

        }
    }

    /**
     * 发布服务
     *
     * @throws InterruptedException
     */
    public void publish() throws InterruptedException {
        //使用netty开启服务
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        serverBootstrap.group(nioEventLoopGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                //心跳机制
                pipeline.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                //拆包粘包
                pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                pipeline.addLast(new LengthFieldPrepender(4));
                //编码解码
//                pipeline.addLast(new NettyKryoDecoder(kryoSeriablize, RpcResponse.class));
//                pipeline.addLast(new NettyKryoEncoder(kryoSeriablize, RpcRequest.class));
                // 解码 bytes=>resp
                 pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                 pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                pipeline.addLast(new ProcessRequestHandler(handlerMap));
            }
        });
        serverBootstrap.bind(serviceIp, servicePort).sync();
        log.info("server started success,host: {},port: {} ", serviceIp, servicePort);
        //服务注册
        handlerMap.keySet().forEach(serviceName -> {
            try {
                zkRegister.register(serviceName, serviceIp + ":" + servicePort);
            } catch (Exception e) {
                log.info("register service failed,{}", e.getMessage());
                e.printStackTrace();
            }
            log.info("register service success,serviceName:{},serviceAddress:{}", serviceName, serviceIp + ":" + servicePort);
        });
    }

}
