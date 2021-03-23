package com.spring.service.impl;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
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
import io.netty.util.CharsetUtil;
import net.sf.jsqlparser.schema.Server;

public class Client  
{  
  private EventLoopGroup loop = new NioEventLoopGroup();
  private String ip;
  private String fitemid; 
  public Bootstrap bootstrap = new Bootstrap();
  public WeldedJunctionServiceImpl weldedJunctionServiceImpl;
  public ConnectionListener CL = new ConnectionListener(this);
  public Client client;
  
  public Client(WeldedJunctionServiceImpl weldedJunctionServiceImpl1) {
	// TODO Auto-generated constructor stub
	  this.weldedJunctionServiceImpl = weldedJunctionServiceImpl1;
	  client = this;
  }

  public Client() {
	// TODO Auto-generated constructor stub
  }

  public static void main( String[] args )  
  {  
    new Client().run();  
  }  
  
  public Runnable start = new Runnable() {
	  public void run(){
		  final TcpClientHandler handler = new TcpClientHandler(client);
		  EventLoopGroup group = new NioEventLoopGroup();
	       try {
	           Bootstrap b = new Bootstrap();
	           b.group(group) // 注册线程池
	           .channel(NioSocketChannel.class) // 使用NioSocketChannel来作为连接用的channel类
	           .remoteAddress(new InetSocketAddress("localhost", 5551)) // 绑定连接端口和host信息
	           .handler(new ChannelInitializer<SocketChannel>() { // 绑定连接初始化器
	           @Override
	           protected void initChannel(SocketChannel ch) throws Exception {
	        	   		ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));    
	        	   		ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));    
	        	   		ch.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));    
	        	   		ch.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8)); 
	                    ch.pipeline().addLast(handler);
	                    weldedJunctionServiceImpl.socketChannel = ch;
	               }
	           });
		   
	           ChannelFuture cf;
			try {
				cf = b.connect().sync();
		        cf.channel().closeFuture().sync(); // 异步等待关闭连接channel
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // 异步连接服务器
	   
	       } finally {
	           try {
				group.shutdownGracefully().sync();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // 释放线程池资源
	       } 
	  }
   };

  public void run() {  
	  try {
		new Thread(start).start();;
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }  
}
