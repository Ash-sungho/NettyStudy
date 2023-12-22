package org.example.echoClient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public final class EchoClient {

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            /*서버 애플리케이션의 부트스트랩 설정과 다르게 이벤트 루프 그룹이 하나만 설정되었다. 클라이언트 애플리케이션은
             * 서버와 달리 서버에 연결된 채널 하나만 존재하기 때문에 이벤트 루프 그룹이 하나다.
             * */
            b.group(group)
                //클라이언트 애플리케이션이 생성하는 채널의 종류를 설정한다. 여기서는 NIO 소켓 채널인 NioSocketChannel 클래스를 설정했다.
                //즉 서버에 연결된 클라이언트의 소켓 채널은 NIO로 동작하게 된다.
                .channel(NioSocketChannel.class)
                //클라이언트 애플리케이션이므로 채널 파이프라인의 설정에 일반 소켓 채널 클래스인 SocketChannel을 설정한다.
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        ChannelPipeline p = socketChannel.pipeline();
                        //수신된 데이터를 에코 서버 핸들러가 되돌려준다
                        p.addLast(new EchoClientHandler());
                    }
                });
            int port = 8888;
            /*비동기 입출력 메서드인 connect를 호출한다. connect메서드는 메서드의 호출 결과로 ChannelFuture객체를 되돌려 주는데
             * 이 객체를 통해서 비동기 메소드의 처리 결과를 확인할 수 있다.
             * ChannelFuture 객체의 sync 메소드는 ChannelFuture 객체의 요청이 완료될 때까지 대기한다.
             * 단 요청이 실패하면 예외를 던진다.즉 connect 메서드의 처리 결과가 완료될 때 까지 다음 라인으로 진행하지 않는다.
             * */
            ChannelFuture f = b.connect("localhost", port).sync();
            f.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }
    }
}
