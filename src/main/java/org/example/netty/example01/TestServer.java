package org.example.netty.example01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Netty简单HTTP服务器
 *
 * 编程模式
 *   1. 采用master-worker模式，编写网络程序
 *   2. 在主程序中创建EventLoopGroup: master/worker
 *   3. 为Channel注册自定义的ChannelInitializer
 *        a. 服务端为NioServerSocketChannel
 *        b. 客户端为NioSocketChannel
 *   4. 在ChannelInitializer中添加自定义的ChannelInBoundHandler
 *        客户端和服务端通信的Channel统一为SocketChannel
 *   5. 用Bootstrap启动服务/客户程序
 *        a. 服务端为ServerBootstrap
 *        b. 客户端为Bootstrap
 *
 * 实验方法
 *   1. curl http://localhost:8899
 *   2. curl -X POST http://localhost:8899
 *   3. 打开浏览器， 访问http://localhost:8899
 */
public class TestServer {

    public static void main(String[] args) throws Exception {
        EventLoopGroup masterGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // BootStrap->EventLoopGroup->ChannelInitializer->InBoundHandler
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(masterGroup, workerGroup).channel(NioServerSocketChannel.class).
                    childHandler(new TestServerInitializer());

            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            masterGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
