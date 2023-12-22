package org.example.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {

    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        ChannelPipeline p = socketChannel.pipeline();
                        //수신된 데이터를 에코 서버 핸들러가 되돌려준다
                        p.addLast(new EchoServerHandler());
                    }
                });
            int port = 8888;

            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();

        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
