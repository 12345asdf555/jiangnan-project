package com;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import io.netty.util.CharsetUtil;

import java.io.*;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    public MyMqttClient mqtt = new MyMqttClient();
    public static String ip = null;
    public static String ip1 = null;
    public static String connet1 = "jdbc:mysql://";
    public static String connet2 = ":3306/";
    public static String connet3 = "?user=";
    public static String connet4 = "&password=";
    public static String connet5 = "&useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT";
    public static String connet;
    public DB_Connectioncode check;
    public static ArrayList<String> listarray1 = new ArrayList<String>();
    public static ArrayList<String> listarray2 = new ArrayList<String>();
    public static ArrayList<String> listarray3 = new ArrayList<String>();
    public Client client = new Client(this);
    public NettyServerHandler nettyServerHandler = new NettyServerHandler();
    public java.sql.Connection conn = null;
    public java.sql.Statement stmt = null;
    public static MysqlDBConnection dbConnection = new MysqlDBConnection();
    public static LiveDataDBConnection liveDataDBConnection = new LiveDataDBConnection();
    //创建缓存线程池，处理PC实时数据
    public static final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    public static final String ipConfigPath = "PC/IPconfig.txt";
    //public static final String ipConfigPath = "IPconfig.txt";

    public Server() {
        //定时线程和一些定时任务处理
        scheduleWorkStart();
        //启动mqtt服务
        emqxClientStart();
        //定时任务启动
        new TaskThread().start();
        //启动PC服务端接收OTC发送的数据
        nettyWorkStart(5551);
    }

    private void scheduleWorkStart() {
        //读取IPconfig配置文件获取ip地址和数据库配置
        try {
            File file = new File(ipConfigPath);
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
                } else {
                    ip1 = line;
                    writetime = 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] values = ip.split(",");

        connet = connet1 + values[0] + connet2 + values[1] + connet3 + values[2] + connet4 + values[3] + connet5;

        nettyServerHandler.ip = ip;
        nettyServerHandler.ip1 = ip1;
        nettyServerHandler.connet = connet;
        client.handler.connet = connet;

        //连接数据库
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(connet);
            stmt = conn.createStatement();
            client.handler.conn = conn;
            client.handler.stmt = stmt;
            NettyServerHandler.mysql.db.conn = conn;
            NettyServerHandler.mysql.db.stmt = stmt;
            NettyServerHandler.android.db.conn = conn;
            NettyServerHandler.android.db.stmt = stmt;
            NettyServerHandler.mysql.db.connet = connet;
            NettyServerHandler.android.db.connet = connet;

        } catch (Exception e) {
            System.out.println("数据库连接异常：" + e);
        }

        //获取最新焊口和焊机统计时间
        check = new DB_Connectioncode(stmt, conn, connet);

        listarray1 = check.getId1();
        listarray2 = check.getId2();
        listarray3 = check.getId3();

        NettyServerHandler.mysql.listarray1 = listarray1;
        NettyServerHandler.mysql.listarray2 = listarray2;
        NettyServerHandler.mysql.listarray3 = listarray3;
        NettyServerHandler.websocket.listarray1 = listarray1;
        NettyServerHandler.websocket.listarray2 = listarray2;
        NettyServerHandler.websocket.listarray3 = listarray3;
        NettyServerHandler.android.listarray1 = listarray1;
        NettyServerHandler.android.listarray2 = listarray2;
        NettyServerHandler.listarray1 = listarray1;
        NettyServerHandler.listarray2 = listarray2;
        NettyServerHandler.listarray3 = listarray3;
        System.out.println("定时任务启动成功！");
    }

    //启动转发器的Netty服务端
    private void nettyWorkStart(final int inetPort) {
        new Thread(new Runnable() {
            final EventLoopGroup bossGroup = new NioEventLoopGroup();
            final EventLoopGroup workerGroup = new NioEventLoopGroup();

            @Override
            public void run() {
                try {
                    ServerBootstrap b = new ServerBootstrap();
                    b.group(bossGroup, workerGroup)
                            .channel(NioServerSocketChannel.class)
                            .option(ChannelOption.SO_BACKLOG, 1024)
                            .childOption(ChannelOption.SO_KEEPALIVE, true)
                            .childHandler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                public void initChannel(SocketChannel chsoc) throws Exception {
                                    chsoc.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                                    chsoc.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
                                    chsoc.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
                                    chsoc.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                                    chsoc.pipeline().addLast(new ReadTimeoutHandler(100), new WriteTimeoutHandler(100), nettyServerHandler);
                                }
                            });
                    //绑定端口，等待同步成功
                    ChannelFuture channelFuture = b.bind(inetPort).sync();
                    if (channelFuture.isSuccess()) {
                        //如果没有成功结束就处理一些事情,结束了就执行关闭服务端等操作
                        System.out.println("PC服务端启动成功,监听端口是：" + inetPort);
                    }
                    //等待服务端关闭监听端口
                    channelFuture.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //释放线程池资源
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                    System.out.println("PC转发器服务端已关闭！");
                }
            }
        }).start();
    }

    //启动mq客户端
    private void emqxClientStart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mqtt.init("mqttclient");
                NettyServerHandler.mqtt = mqtt;
                Websocket.mqtt = mqtt;
                mqtt.subTopic("weldmes/downparams");
            }
        }).start();
    }

    //多层级转发
    public Runnable sockettran = new Runnable() {
        @Override
        public void run() {
            if (ip1 != null) {
                client.run();
            }
        }
    };

    public static void main(String[] args) throws IOException {
        new Server();
    }

}


