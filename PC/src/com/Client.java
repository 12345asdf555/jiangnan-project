package com;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

public class Client {
    private static final EventLoopGroup loop = new NioEventLoopGroup();
    private static final String inetHost = "localhost";
    public static Bootstrap bootstrap = new Bootstrap();
    public static ConnectionListener CL = new ConnectionListener();
    public static TcpClientHandler handler = new TcpClientHandler();

    public static Bootstrap createBootstrap(Bootstrap bootstrap, EventLoopGroup eventLoop) {
        if (bootstrap != null) {
            bootstrap.group(eventLoop);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast("ping", new IdleStateHandler(60, 60, 60 * 10, TimeUnit.SECONDS));
                    socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
                    socketChannel.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                    socketChannel.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
                    socketChannel.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                    socketChannel.pipeline().addLast(
                            new ReadTimeoutHandler(100),
                            new WriteTimeoutHandler(100),
                            handler);
                    ConnectionListener.socketChannel = socketChannel;
                }
            });
            bootstrap.remoteAddress(inetHost, 5556);
            bootstrap.connect().addListener(CL);
        }
        return bootstrap;
    }

    public static void run() {
        createBootstrap(bootstrap, loop);
    }
}
