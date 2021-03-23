package com;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
@Sharable
public class NettyWebSocketHandler extends SimpleChannelInboundHandler<Object> {

	public HashMap<String, SocketChannel> socketlist = new HashMap<>();
	public ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	private static final Logger logger = Logger.getLogger(WebSocketServerHandshaker.class.getName());
	private WebSocketServerHandshaker handsharker;
	private String socketfail;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 添加
		//group.add(ctx.channel());
		////System.out.println("客户端与服务端连接开启：" + ctx.channel().remoteAddress().toString());
	}

	/**
	 * channel 通道 Inactive 不活跃的 当客户端主动断开服务端的链接后，这个通道就是不活跃的。也就是说客户端与服务端关闭了通信通道并且不可以传输数据
	 */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// 移除
		//group.remove(ctx.channel());
		////System.out.println("客户端与服务端连接关闭：" + ctx.channel().remoteAddress().toString());
	}
	/**
	 * exception 异常 Caught 抓住 抓住异常，当发生异常的时候，可以做一些相应的处理，比如打印日志、关闭链接
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		//cause.printStackTrace();
		ctx.close();
	}
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	/**
	 * 接受数据
	 */
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 如果是HTTP请求
		if (msg instanceof FullHttpRequest) {
			//handleHttpRequest(ctx, (FullHttpRequest) msg);
		}
		// WEBSOCKET接入
		else if (msg instanceof WebSocketFrame) {
			handleWebSocketFrame(ctx, (WebSocketFrame) msg);
		}
	}
	/**
	 * 处理HTTP请求
	 * 
	 * @param ctx
	 * @param msg
	 */
	/*private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        // 如果解码失败,返回异常
        if (!req.getDecoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
            sendHttpResponse(ctx, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        // 构造握手相应
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://localhost:8080/websocket", null, Boolean.FALSE);
        handsharker = wsFactory.newHandshaker(req);
        if (handsharker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
        } else {
            handsharker.handshake(ctx.channel(), req);
        }
    }*/


	/**
	 * 处理WEBSOCKET请求
	 * 
	 * @param ctx
	 * @param frame
	 */
	private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
		// 判断链路是否关闭
		if (frame instanceof CloseWebSocketFrame) {
			handsharker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
			return;
		}
		if (frame instanceof PingWebSocketFrame) {
			new PongWebSocketFrame(frame.content().retain());
			return;
		}
		// 只支持文本消息,不支持二进制消息
		if (!(frame instanceof TextWebSocketFrame)) {
			throw new UnsupportedOperationException(
					String.format("%s frame type not supported", frame.getClass().getName()));
		}

		String str = ((TextWebSocketFrame) frame).text();
		
		//福尼斯焊机处理
		if(str.substring(0,1).equals("0") || str.substring(0,1).equals("1")){
			synchronized (socketlist) {
				ArrayList<String> listarraybuf = new ArrayList<String>();
				boolean ifdo = false;

				Iterator<Entry<String, SocketChannel>> webiter = socketlist.entrySet().iterator();
				while(webiter.hasNext()){
					try{
						Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
						socketfail = entry.getKey();
						SocketChannel socketcon = entry.getValue();
						String[] socketip1 = socketcon.toString().split("/");
						String[] socketip2 = socketip1[1].split(":");
						String socketip = socketip2[0];
						//if(!socketip.equals("192.168.1.101")){
						if(!socketip.equals("121.196.222.216")){
							socketcon.writeAndFlush(str).sync();
						}

					}catch (Exception e) {
						listarraybuf.add(socketfail);
						ifdo = true;
					}
				}

				if(ifdo){
					for(int i=0;i<listarraybuf.size();i++){
						socketlist.remove(listarraybuf.get(i));
					}
				}
			}
		}
		//OTC、松下下发指令
		else if(str.substring(0,2).equals("7E") || str.substring(0,6).equals("FE5AA5")){

			synchronized (socketlist) {
				ArrayList<String> listarraybuf = new ArrayList<String>();
				boolean ifdo = false;

				Iterator<Entry<String, SocketChannel>> webiter = socketlist.entrySet().iterator();
				while(webiter.hasNext()){
					try{
						Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
						socketfail = entry.getKey();
						SocketChannel socketcon = entry.getValue();
						String[] socketip1 = socketcon.toString().split("/");
						String[] socketip2 = socketip1[1].split(":");
						String socketip = socketip2[0];
						//if(!socketip.equals("192.168.1.101")){
						if(!socketip.equals("121.196.222.216")){
							socketcon.writeAndFlush(str).sync();
						}

					}catch (Exception e) {
						listarraybuf.add(socketfail);
						ifdo = true;
					}
				}

				if(ifdo){
					for(int i=0;i<listarraybuf.size();i++){
						socketlist.remove(listarraybuf.get(i));
					}
				}
			}

		}
		// ctx.channel().write(new TextWebSocketFrame(request + " , 欢迎使用netty
		// websocket 服务,现在时刻是: ")
		// + new java.util.Date().toString());
	}

	/**
	 * 接收客户端发送的消息 channel 通道 Read 读 简而言之就是从通道中读取数据，也就是服务端接收客户端发来的数据。但是这个数据在不进行解码时它是ByteBuf类型的
	 *//*
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 传统的HTTP接入
		if (msg instanceof FullHttpRequest) {
			handleHttpRequest(ctx, ((FullHttpRequest) msg));
			// WebSocket接入
		} else if (msg instanceof WebSocketFrame) {
			//System.out.println(handshaker.uri());
			if("anzhuo".equals(ctx.attr(AttributeKey.valueOf("type")).get())){
				handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
			}else{
				handlerWebSocketFrame2(ctx, (WebSocketFrame) msg);
			}
		}
	}*/
	/*private void handlerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
	// 判断是否关闭链路的指令
		if (frame instanceof CloseWebSocketFrame) {
			//System.out.println(1);
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
			return;
		}
	// 判断是否ping消息
		if (frame instanceof PingWebSocketFrame) {
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		// 本例程仅支持文本消息，不支持二进制消息
		if (!(frame instanceof TextWebSocketFrame)) {
			//System.out.println("本例程仅支持文本消息，不支持二进制消息");
			throw new UnsupportedOperationException(
			String.format("%s frame types not supported", frame.getClass().getName()));
		}
		// 返回应答消息
		String request = ((TextWebSocketFrame) frame).text();
		//System.out.println("服务端收到：" + request);
		if (logger.isLoggable(Level.FINE)) {
			logger.fine(String.format("%s received %s", ctx.channel(), request));
		}
		TextWebSocketFrame tws = new TextWebSocketFrame(new Date().toString() + ctx.channel().id() + "：" + request);
		// 群发
		group.writeAndFlush(tws);
		// 返回【谁发的发给谁】
		// ctx.channel().writeAndFlush(tws);
	}

	private void handlerWebSocketFrame2(ChannelHandlerContext ctx, WebSocketFrame frame) {
	// 判断是否关闭链路的指令
		if (frame instanceof CloseWebSocketFrame) {
			handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
			return;
		}
		// 判断是否ping消息
		if (frame instanceof PingWebSocketFrame) {
			ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
			return;
		}
		// 本例程仅支持文本消息，不支持二进制消息
		if (!(frame instanceof TextWebSocketFrame)) {
			//System.out.println("本例程仅支持文本消息，不支持二进制消息");
			throw new UnsupportedOperationException(
			String.format("%s frame types not supported", frame.getClass().getName()));
		}
		// 返回应答消息
		String request = ((TextWebSocketFrame) frame).text();
		//System.out.println("服务端2收到：" + request);
		if (logger.isLoggable(Level.FINE)) {
			logger.fine(String.format("%s received %s", ctx.channel(), request));
		}
		TextWebSocketFrame tws = new TextWebSocketFrame(new Date().toString() + ctx.channel().id() + "：" + request);
		// 群发
		group.writeAndFlush(tws);
		// 返回【谁发的发给谁】
		// ctx.channel().writeAndFlush(tws);
	}*/
	/*private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
	// 如果HTTP解码失败，返回HHTP异常
		if (!req.getDecoderResult().isSuccess() || (!"websocket".equals(req.headers().get("Upgrade")))) {
			sendHttpResponse(ctx, req,
			new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
			return;
		}
		//获取url后置参数
		HttpMethod method=req.getMethod();
		String uri=req.getUri();
		QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
		Map<String, List<String>> parameters = queryStringDecoder.parameters();
		//System.out.println(parameters.get("request").get(0));
		if(method==HttpMethod.GET&&"/webssss".equals(uri)){
			//....处理
			ctx.attr(AttributeKey.valueOf("type")).set("anzhuo");
		}else if(method==HttpMethod.GET&&"/websocket".equals(uri)){
			//...处理
			ctx.attr(AttributeKey.valueOf("type")).set("live");
		}
		// 构造握手响应返回，本机测试
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
		"ws://"+req.headers().get(HttpHeaders.Names.HOST)+uri, null, false);
		handshaker = wsFactory.newHandshaker(req);
		if (handshaker == null) {
			WebSocketServerHandshakerFactory.sendUnsupportedWebSocketVersionResponse(ctx.channel());
		} else {
			handshaker.handshake(ctx.channel(), req);
		}
	}
	private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, DefaultFullHttpResponse res) {
	// 返回应答给客户端
		if (res.getStatus().code() != 200) {
			ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
			res.content().writeBytes(buf);
			buf.release();
		}
			// 如果是非Keep-Alive，关闭连接
			ChannelFuture f = ctx.channel().writeAndFlush(res);
			if (!HttpHeaders.isKeepAlive(req) || res.getStatus().code() != 200) {
			f.addListener(ChannelFutureListener.CLOSE);
		}
	}*/

}
