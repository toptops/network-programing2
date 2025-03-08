package com.network.girl.chapter1.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String sendMessage = "Hello Netty";
        ByteBuf messageBuffer = Unpooled.buffer();
        messageBuffer.writeBytes(sendMessage.getBytes());

        StringBuilder builder = new StringBuilder();
        builder.append("전송할 문자열[");
        builder.append(sendMessage);
        builder.append("]");

        System.out.println(builder.toString());
        ctx.writeAndFlush(messageBuffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset());

        StringBuilder builder = new StringBuilder();
        builder.append("수신할 문자열[");
        builder.append(readMessage);
        builder.append("]");

        System.out.println(builder.toString());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.close();
    }
}
