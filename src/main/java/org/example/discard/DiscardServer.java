package org.example.discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class DiscardServer {

    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        //접속된 클라이언트로부터 수신된 데이터를 처리할 핸들러를 지정한다
                        p.addLast(new DiscardServerHandler());
                    }
                });
            int port = 8888;

            //부트스트랩 클래스의 bind메서드로 접속할 포트를 지정한다.
            ChannelFuture f = b.bind(port).sync();
            f.channel().closeFuture().sync();

        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }


    }

}
