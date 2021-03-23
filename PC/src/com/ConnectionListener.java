package com;

import java.util.concurrent.TimeUnit;

import com.Client;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;
import io.netty.channel.socket.SocketChannel;

public class ConnectionListener implements ChannelFutureListener {
	private Client client;  
	public SocketChannel socketChannel;
	public boolean first = true;
	public ConnectionListener(Client client) {
	    this.client = client;  
	}  
	@Override
	public void operationComplete(ChannelFuture channelFuture) throws Exception {
		// TODO Auto-generated method stub
		if (!channelFuture.isSuccess()) {  
		      ////System.out.println("Reconnect");
			  final EventLoop loop = channelFuture.channel().eventLoop();  
		      loop.schedule(new Runnable() {  
		        @Override  
		        public void run() {  
		          client.createBootstrap(new Bootstrap(), loop);  
		        }  
		      }, 1L, TimeUnit.SECONDS);
		    }else{
				  client.server.nettyServerHandler.socketchannel = socketChannel;
				  first = false;
			}  
		}  
	}

