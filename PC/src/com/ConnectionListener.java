package com;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import io.netty.channel.socket.SocketChannel;

import java.util.concurrent.TimeUnit;

public class ConnectionListener implements ChannelFutureListener {
	public static SocketChannel socketChannel;
	public boolean first = true;

	@Override
	public void operationComplete(ChannelFuture channelFuture) throws Exception {
		// TODO Auto-generated method stub
		if (!channelFuture.isSuccess()) {  
		      ////System.out.println("Reconnect");
			  final EventLoop loop = channelFuture.channel().eventLoop();  
		      loop.schedule(new Runnable() {  
		        @Override  
		        public void run() {
					Client.createBootstrap(new Bootstrap(), loop);
		        }  
		      }, 1L, TimeUnit.SECONDS);
		    }else{
				  NettyServerHandler.socketchannel = socketChannel;
				  first = false;
			}  
		}  
	}

