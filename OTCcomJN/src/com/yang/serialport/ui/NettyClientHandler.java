package com.yang.serialport.ui;

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
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.util.CharsetUtil;

import java.io.*;

//作为客户端连接服务器
public class NettyClientHandler {
    public MainFrame mainFrame;
    public NettyServerHandler NS;
    public NettyServerHandlerF NSF;
    private EventLoopGroup loop = new NioEventLoopGroup();
    private String ip;
    private String fitemid;
    public Bootstrap bootstrap = new Bootstrap();
    public NettyClientListener CL = new NettyClientListener(this);

    public NettyClientHandler(NettyServerHandler NS, NettyServerHandlerF NSF, MainFrame mainFrame) {
        this.NS = NS;
        this.NSF = NSF;
        this.mainFrame = mainFrame;
    }

    public NettyClientHandler() {
    }

//    public static void main(String[] args) {
//        new Clientconnect().run();
//    }

    public void createBootstrap(Bootstrap bootstrap, EventLoopGroup eventLoop) {
        try {
            if (bootstrap != null) {
                final TcpClientHandler handler = new TcpClientHandler(this);

                try {
                    File file = new File(MainFrame.ipConfigPath);
                    String filePath = file.getCanonicalPath();
                    FileInputStream in = new FileInputStream(filePath);
                    InputStreamReader inReader = new InputStreamReader(in, "UTF-8");
                    BufferedReader bufReader = new BufferedReader(inReader);
                    String line = null;
                    int writetime = 0;

                    while ((line = bufReader.readLine()) != null) {
                        if (writetime == 0) {
                            ip = line;
                            writetime++;
                        } else if (writetime == 1) {
                            fitemid = line;
                            writetime++;
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                bootstrap.group(eventLoop);
                bootstrap.channel(NioSocketChannel.class);
                bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                        socketChannel.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
                        socketChannel.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
                        socketChannel.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                        socketChannel.pipeline().addLast(
                                new ReadTimeoutHandler(100),
                                new WriteTimeoutHandler(100),
                                handler);
                        CL.socketChannel = socketChannel;
                    }
                });
                bootstrap.remoteAddress(ip, 5551);
                bootstrap.connect().addListener(CL);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("OTC采集器netty客户端创建异常！" + e);
        }
    }

    public void run() {
        createBootstrap(bootstrap, loop);
    }
}
