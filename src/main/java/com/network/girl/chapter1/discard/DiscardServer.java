package com.network.girl.chapter1.discard;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * chapter1, Discard 서버
 * - 아무것도 안함..!? 접속만 가능핟
 */
public class DiscardServer {
    public static void main(String[] args) throws Exception {

        /**
         * EventLoop? 하나의 스레드를 지칭하며 해당 스레드에서 이벤트를 지속적을 처리하는걸 의미
         * 그럼 EventLoopGroup? EventLoop들의 Group이며 n개의 스레드에서 이벤트를 처리를 의미
         *
         * 하지만.. 로직에서 지연시간이 발생한다면? 결국 느려진다..!
         * 오랜만에 기억 되새김질하기 빡세다..
         */
        // 채널 연결을 담당하는 EventLoopGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 채널 이벤트를 담당하는 EventLoopGroup
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 실제 서버 소켓을 설정하고 실행하는 핵심적인 클래스, 옵션등을 설정할 수 있다.
            ServerBootstrap b = new ServerBootstrap();
            // group 설정의 경우 연결과 로직 처리 그룹을 분리하여 지정
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    // 이벤트 핸들러 설정
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new DiscardServerHandler());
                        }
                    });

            // 서버 포트 바인딩
            ChannelFuture f = b.bind(8888).sync();
            // 서버 종료되기전까지 대기
            // 결국 어디선가는 메인 쓰레드를 종료시키면 안되기 때문..
            f.channel().closeFuture().sync();
        }
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
