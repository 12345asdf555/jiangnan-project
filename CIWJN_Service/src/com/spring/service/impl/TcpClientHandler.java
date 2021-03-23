package com.spring.service.impl;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;

public class TcpClientHandler extends SimpleChannelInboundHandler{

	public Client client;
	public String msg;
	
	public TcpClientHandler(Client client) {
		// TODO Auto-generated constructor stub
		this.client = client;
	}
	
	public TcpClientHandler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void messageReceived(ChannelHandlerContext arg0, Object arg1) throws Exception {
		// TODO Auto-generated method stub
		msg = (String) arg1;
		client.weldedJunctionServiceImpl.data = msg;
		//arg0.close();
	}
	
	@Override  
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {  
	
    }  
	
}
