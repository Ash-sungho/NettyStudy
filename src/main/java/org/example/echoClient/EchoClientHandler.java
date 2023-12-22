package org.example.echoClient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.charset.Charset;

//입력된 데이텉를 처리하는 이벤트 핸들러인 ChannelInboundHandlerAdapter를 상속받게 지정한다.
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    //channelActive 아밴투눈 ChanneInboundHandler에 정의된 이벤트로써 소켓 채널이 최초 활성화 되었을때 실행한다.
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String sendMessage = "Hello Netty!";
        ByteBuf messageBuffer = Unpooled.buffer();
        messageBuffer.writeBytes(sendMessage.getBytes());

        String builder = "전송한 문자열 ["
            + sendMessage
            + "]";

        System.out.println(builder);
        //writeAndFlush 메서드는 내부적으로 데이터 기록과 전송의 두가지 메서드를 호출한다.첫번째는 채널에 데이터를 기록하는
        //write 메서드이며 두번째는 채널에 기록된 데이터를 서버로 전송하는 flush 메서드이다.
        ctx.writeAndFlush(messageBuffer);
    }

    //서버로부터 수신된 데이터가 있을 때 호출되는 네티 이벤트 메서드다.
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //수신된 데이터를 가지고 있는 네티의 바이트 버퍼객체로부터 문자열 데이터를 읽어온다
        String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset());
        //수신된 문자열을 콘솔로 출력한다.
        System.out.println("수신한 문자열 [" + readMessage + "]");
    }

    //수신된 데이터를 모두 읽었을때 호출되는 메서드다. channelRead의 수행이 완료되고 나서 자동으로 호출된다.
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        //수신된 데이터를 모두 읽은 후 서버와 연결된 채널을 닫는다. 이후 데이터 송수신 채널은 닫히고 클라이언트 프로그램은 종료된다.
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
