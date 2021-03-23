package com.yang.serialport.ui;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import io.netty.channel.socket.SocketChannel;

//netty连接服务器,若断开自动检测重连
public class NettyClientListener implements ChannelFutureListener {
    public NettyClientHandler client;
    public SocketChannel socketChannel;

    public NettyClientListener(NettyClientHandler client) {
        this.client = client;
    }

    public NettyClientListener() {}

    /**
     * netty客户端心跳监听
     * @param channelFuture
     * @throws Exception
     */
    @Override
    public void operationComplete(ChannelFuture channelFuture) throws Exception {
        if (!channelFuture.isSuccess()) {
            //连接的服务端已断开，重启客户端
            final EventLoop loop = channelFuture.channel().eventLoop();
            loop.schedule(new Runnable() {
                @Override
                public void run() {
                    //开始连接
                    client.createBootstrap(new Bootstrap(), loop);
                }
            }, 1L, TimeUnit.SECONDS);
        } else {
            //通道连接成功，并赋值
            StaticClass.chcli = socketChannel;
        }
    }

}

