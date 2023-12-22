package org.example.echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.charset.Charset;

//입력된 데이텉를 처리하는 이벤트 핸들러인 ChannelInboundHandlerAdapter를 상속받게 지정한다.
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    //데이터 수신 이벤트 처리 메서드다. 클라이언트로부터 데이터의 수신이 이루어졌을때 네티가 자동으로 호출하는 이벤트 메서드이다.
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //수신된 데이터를 가지고 있는 네티의 바이트 버퍼객체로부터 문자열 데이터를 읽어온다
        String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset());
        //수신된 문자열을 콘솔로 출력한다.
        System.out.println("수신한 문자열 [" + readMessage + "]");
        //ctx는 ChannelHandlerContext 인터페이스의 객체로서 채널 파이프라인에 대한 이벤트를 처리한다.
        ctx.write(readMessage);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
