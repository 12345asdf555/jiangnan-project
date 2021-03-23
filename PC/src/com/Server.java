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
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    public MyMqttClient mqtt = new MyMqttClient();
    public String str = "";
    public Socket socket = null;
    public String ip = null;
    public String ip1 = null;
    public String connet1 = "jdbc:mysql://";
    public String connet2 = ":3306/";
    public String connet3 = "?user=";
    public String connet4 = "&password=";
    public String connet5 = "&useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT";
    public String connet;
    public DB_Connectioncode check;
    public ArrayList<String> listarray1 = new ArrayList<String>();
    public ArrayList<String> listarray2 = new ArrayList<String>();
    public ArrayList<String> listarray3 = new ArrayList<String>();
    public HashMap<String, SocketChannel> socketlist = new HashMap<>();
    public int socketcount = 0;
    public Client client = new Client(this);
    public NettyServerHandler nettyServerHandler = new NettyServerHandler();
    private NettyWebSocketHandler NWS = new NettyWebSocketHandler();
    private Connection c;
    public java.sql.Connection conn = null;
    public java.sql.Statement stmt = null;
    private Date time;
    private ArrayList<String> dbdata;
    public static MysqlDBConnection dbConnection = new MysqlDBConnection();
    //创建缓存线程池，处理PC实时数据
    public static final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    public Server() {
        //定时线程和一些定时任务处理
        scheduleWork();
        //启动PC服务端接收OTC发送的数据
        new Thread(socketstart).start();
        //启动mqtt服务
        new Thread(mqttConnect).start();
    }

    private void scheduleWork() {
        //读取IPconfig配置文件获取ip地址和数据库配置
        try {
            //File file = new File("IPconfig.txt");
            File file = new File("PC/IPconfig.txt");
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

        nettyServerHandler.ip = this.ip;
        nettyServerHandler.ip1 = this.ip1;
        nettyServerHandler.connet = this.connet;
        client.handler.connet = this.connet;

        //连接数据库
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(connet);
            stmt = conn.createStatement();
            nettyServerHandler.conn = conn;
            nettyServerHandler.stmt = stmt;
            client.handler.conn = this.conn;
            client.handler.stmt = this.stmt;
            nettyServerHandler.mysql.db.conn = conn;
            nettyServerHandler.mysql.db.stmt = stmt;
            nettyServerHandler.android.db.conn = conn;
            nettyServerHandler.android.db.stmt = stmt;
            nettyServerHandler.mysql.db.connet = connet;
            nettyServerHandler.android.db.connet = connet;

        } catch (Exception e) {
            System.out.println("数据库连接异常："+e);
            //e.printStackTrace();
        }

        //开启线程每小时更新三张状态表
        Date date = new Date();
        String nowtime = DateTools.format("HH:mm:ss", date);
        String[] timesplit = nowtime.split(":");
        String hour = timesplit[0];

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hour) + 1); // 控制时
        calendar.set(Calendar.MINUTE, 00);    // 控制分
        calendar.set(Calendar.SECOND, 00);    // 控制秒
        time = calendar.getTime();

        Timer tExit1 = null;
        tExit1 = new Timer();
        tExit1.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    conn = DriverManager.getConnection(connet);
                    stmt = conn.createStatement();
                    nettyServerHandler.stmt = stmt;

                    Class.forName("com.mysql.cj.jdbc.Driver");
                    conn = DriverManager.getConnection(connet);
                    stmt = conn.createStatement();

                    //获取四张状态表初试时间，若为空赋默认值
                    Date date = new Date();
                    String nowtimefor = DateTools.format("yyyy-MM-dd", date);
                    String nowtime = DateTools.format("HH:mm:ss", date);
                    String[] timesplit = nowtime.split(":");
                    String hour = timesplit[0];
                    String time2 = nowtimefor + " " + hour + ":00:00";//当前时间【小时】
                    Date d1 = new Date((DateTools.parse("yyyy-MM-dd HH:mm:ss", time2).getTime()) - 3600000);
                    String time3 = DateTools.format("yyyy-MM-dd HH:mm:ss", d1);//当前时间的上一个小时

                    String timework = null;
                    String timestandby = null;
                    String timealarm = null;
                    String timewarn = null;
                    String sqlfirstwork = "SELECT tb_work.fUploadDataTime FROM tb_work ORDER BY tb_work.fUploadDataTime DESC LIMIT 0,1";
                    String sqlfirststandby = "SELECT tb_standby.fUploadDataTime FROM tb_standby ORDER BY tb_standby.fUploadDataTime DESC LIMIT 0,1";
                    String sqlfirstalarm = "SELECT tb_alarm.fUploadDataTime FROM tb_alarm ORDER BY tb_alarm.fUploadDataTime DESC LIMIT 0,1";
                    String sqlfirstwarn = "SELECT tb_warn.fUploadDataTime FROM tb_warn ORDER BY tb_warn.fUploadDataTime DESC LIMIT 0,1";
                    ResultSet rs1 = stmt.executeQuery(sqlfirstwork);
                    while (rs1.next()) {
                        timework = rs1.getString("fUploadDataTime");
                    }
                    ResultSet rs2 = stmt.executeQuery(sqlfirststandby);
                    while (rs2.next()) {
                        timestandby = rs2.getString("fUploadDataTime");
                    }
                    ResultSet rs3 = stmt.executeQuery(sqlfirstalarm);
                    while (rs3.next()) {
                        timealarm = rs3.getString("fUploadDataTime");
                    }
                    ResultSet rs4 = stmt.executeQuery(sqlfirstwarn);
                    while (rs4.next()) {
                        timewarn = rs4.getString("fUploadDataTime");
                    }

                    if (timework == null || "".equals(timework)) {
                        timework = time3;
                    }
                    if (timestandby == null || "".equals(timestandby)) {
                        timestandby = time3;
                    }
                    if (timealarm == null || "".equals(timealarm)) {
                        timealarm = time3;
                    }
                    if (timewarn == null || "".equals(timewarn)) {
                        timewarn = time3;
                    }

                    //更新四张状态表
                    String sqlstandby = "INSERT INTO tb_standby(tb_standby.fwelder_id,tb_standby.fgather_no,tb_standby.fmachine_id,tb_standby.fjunction_id,"
                            + "tb_standby.fitemid,tb_standby.felectricity,tb_standby.fvoltage,tb_standby.frateofflow,tb_standby.fstandbytime,tb_standby.fstarttime,tb_standby.fendtime,tb_standby.fwelder_no,tb_standby.fjunction_no,tb_standby.fweld_no,tb_standby.fchannel,tb_standby.fmax_electricity,tb_standby.fmin_electricity,tb_standby.fmax_voltage,tb_standby.fmin_voltage,tb_standby.fwelder_itemid,tb_standby.fjunction_itemid,tb_standby.fmachine_itemid,tb_standby.fwirefeedrate,tb_standby.fmachinemodel,tb_standby.fwirediameter,tb_standby.fmaterialgas,tb_standby.fwmax_electricity,tb_standby.fwmin_electricity,tb_standby.fwmax_voltage,tb_standby.fwmin_voltage,tb_standby.fstatus,tb_standby.fsolder_layer,tb_standby.fweld_bead) SELECT "
                            + "tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fitemid,"
                            + "AVG(tb_live_data.felectricity),AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),'" + time3 + "','" + time2 + "',tb_live_data.fwelder_no,tb_live_data.fjunction_no,tb_live_data.fweld_no,tb_live_data.fchannel,tb_live_data.fmax_electricity,tb_live_data.fmin_electricity,tb_live_data.fmax_voltage,tb_live_data.fmin_voltage,tb_live_data.fwelder_itemid,tb_live_data.fjunction_itemid,tb_live_data.fmachine_itemid,AVG(tb_live_data.fwirefeedrate),tb_live_data.fmachinemodel,tb_live_data.fwirediameter,tb_live_data.fmaterialgas,AVG(tb_live_data.fwmax_electricity),AVG(tb_live_data.fwmin_electricity),AVG(tb_live_data.fwmax_voltage),AVG(tb_live_data.fwmin_voltage),tb_live_data.fstatus,tb_live_data.fsolder_layer,tb_live_data.fweld_bead FROM tb_live_data "
                            + "WHERE tb_live_data.fstatus = '0' AND tb_live_data.FWeldTime BETWEEN '" + timestandby + "' AND '" + time2 + "' "
                            + "GROUP BY tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fjunction_id,tb_live_data.fstatus,tb_live_data.fmachine_id,tb_live_data.fsolder_layer,tb_live_data.fweld_bead";

                    String sqlwork = "INSERT INTO tb_work(tb_work.fwelder_id,tb_work.fgather_no,tb_work.fmachine_id,tb_work.fjunction_id,tb_work.fitemid,"
                            + "tb_work.felectricity,tb_work.fvoltage,tb_work.frateofflow,tb_work.fworktime,tb_work.fstarttime,tb_work.fendtime,tb_work.fwelder_no,tb_work.fjunction_no,tb_work.fweld_no,tb_work.fchannel,tb_work.fmax_electricity,tb_work.fmin_electricity,tb_work.fmax_voltage,tb_work.fmin_voltage,tb_work.fwelder_itemid,tb_work.fjunction_itemid,tb_work.fmachine_itemid,tb_work.fwirefeedrate,tb_work.fmachinemodel,tb_work.fwirediameter,tb_work.fmaterialgas,tb_work.fwmax_electricity,tb_work.fwmin_electricity,tb_work.fwmax_voltage,tb_work.fwmin_voltage,tb_work.fstatus,tb_work.fsolder_layer,tb_work.fweld_bead) SELECT tb_live_data.fwelder_id,"
                            + "tb_live_data.fgather_no,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fitemid,AVG(tb_live_data.felectricity),"
                            + "AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),'" + time3 + "','" + time2 + "',tb_live_data.fwelder_no,tb_live_data.fjunction_no,tb_live_data.fweld_no,tb_live_data.fchannel,tb_live_data.fmax_electricity,tb_live_data.fmin_electricity,tb_live_data.fmax_voltage,tb_live_data.fmin_voltage,tb_live_data.fwelder_itemid,tb_live_data.fjunction_itemid,tb_live_data.fmachine_itemid,AVG(tb_live_data.fwirefeedrate),tb_live_data.fmachinemodel,tb_live_data.fwirediameter,tb_live_data.fmaterialgas,AVG(tb_live_data.fwmax_electricity),AVG(tb_live_data.fwmin_electricity),AVG(tb_live_data.fwmax_voltage),AVG(tb_live_data.fwmin_voltage),tb_live_data.fstatus,tb_live_data.fsolder_layer,tb_live_data.fweld_bead FROM tb_live_data "
                            + "WHERE (tb_live_data.fstatus = '3' OR fstatus= '5' OR fstatus= '7' OR fstatus= '99') AND tb_live_data.FWeldTime BETWEEN '" + timework + "' AND '" + time2 + "' "
                            + "GROUP BY tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fjunction_id,tb_live_data.fstatus,tb_live_data.fmachine_id,tb_live_data.fsolder_layer,tb_live_data.fweld_bead";

                    String sqlwarn = "INSERT INTO tb_warn(tb_warn.fwelder_id,tb_warn.fgather_no,tb_warn.fmachine_id,tb_warn.fjunction_id,"
                            + "tb_warn.fitemid,tb_warn.felectricity,tb_warn.fvoltage,tb_warn.frateofflow,tb_warn.fwarntime,tb_warn.fstarttime,tb_warn.fendtime,tb_warn.fwelder_no,tb_warn.fjunction_no,tb_warn.fweld_no,tb_warn.fchannel,tb_warn.fmax_electricity,tb_warn.fmin_electricity,tb_warn.fmax_voltage,tb_warn.fmin_voltage,tb_warn.fwelder_itemid,tb_warn.fjunction_itemid,tb_warn.fmachine_itemid,tb_warn.fwirefeedrate,tb_warn.fmachinemodel,tb_warn.fwirediameter,tb_warn.fmaterialgas,tb_warn.fwmax_electricity,tb_warn.fwmin_electricity,tb_warn.fwmax_voltage,tb_warn.fwmin_voltage,tb_warn.fstatus,tb_warn.fsolder_layer,tb_warn.fweld_bead) SELECT "
                            + "tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fitemid,"
                            + "AVG(tb_live_data.felectricity),AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),'" + time3 + "','" + time2 + "',tb_live_data.fwelder_no,tb_live_data.fjunction_no,tb_live_data.fweld_no,tb_live_data.fchannel,tb_live_data.fmax_electricity,tb_live_data.fmin_electricity,tb_live_data.fmax_voltage,tb_live_data.fmin_voltage,tb_live_data.fwelder_itemid,tb_live_data.fjunction_itemid,tb_live_data.fmachine_itemid,AVG(tb_live_data.fwirefeedrate),tb_live_data.fmachinemodel,tb_live_data.fwirediameter,tb_live_data.fmaterialgas,AVG(tb_live_data.fwmax_electricity),AVG(tb_live_data.fwmin_electricity),AVG(tb_live_data.fwmax_voltage),AVG(tb_live_data.fwmin_voltage),tb_live_data.fstatus,tb_live_data.fsolder_layer,tb_live_data.fweld_bead FROM tb_live_data "
                            + "WHERE tb_live_data.fstatus != '0' AND tb_live_data.fstatus != '3' AND tb_live_data.fstatus != '5' AND tb_live_data.fstatus != '7' AND tb_live_data.FWeldTime BETWEEN '" + timewarn + "' AND '" + time2 + "' "
                            + "GROUP BY tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fjunction_id,tb_live_data.fstatus,tb_live_data.fmachine_id,tb_live_data.fsolder_layer,tb_live_data.fweld_bead";

                    String sqlalarm = "INSERT INTO tb_alarm(tb_alarm.fwelder_id,tb_alarm.fgather_no,tb_alarm.fmachine_id,tb_alarm.fjunction_id,tb_alarm.fitemid,"
                            + "tb_alarm.felectricity,tb_alarm.fvoltage,tb_alarm.frateofflow,tb_alarm.falarmtime,tb_alarm.fstarttime,tb_alarm.fendtime,tb_alarm.fwelder_no,tb_alarm.fjunction_no,tb_alarm.fweld_no,tb_alarm.fchannel,tb_alarm.fmax_electricity,tb_alarm.fmin_electricity,tb_alarm.fmax_voltage,tb_alarm.fmin_voltage,tb_alarm.fwelder_itemid,tb_alarm.fjunction_itemid,tb_alarm.fmachine_itemid,tb_alarm.fwirefeedrate,tb_alarm.fmachinemodel,tb_alarm.fwirediameter,tb_alarm.fmaterialgas,tb_alarm.fwmax_electricity,tb_alarm.fwmin_electricity,tb_alarm.fwmax_voltage,tb_alarm.fwmin_voltage,tb_alarm.fstatus,tb_alarm.fsolder_layer,tb_alarm.fweld_bead) SELECT tb_live_data.fwelder_id,"
                            + "tb_live_data.fgather_no,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fitemid,AVG(tb_live_data.felectricity),"
                            + "AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),'" + time3 + "','" + time2 + "',tb_live_data.fwelder_no,tb_live_data.fjunction_no,tb_live_data.fweld_no,tb_live_data.fchannel,tb_live_data.fmax_electricity,tb_live_data.fmin_electricity,tb_live_data.fmax_voltage,tb_live_data.fmin_voltage,tb_live_data.fwelder_itemid,tb_live_data.fjunction_itemid,tb_live_data.fmachine_itemid,AVG(tb_live_data.fwirefeedrate),tb_live_data.fmachinemodel,tb_live_data.fwirediameter,tb_live_data.fmaterialgas,AVG(tb_live_data.fwmax_electricity),AVG(tb_live_data.fwmin_electricity),AVG(tb_live_data.fwmax_voltage),AVG(tb_live_data.fwmin_voltage),tb_live_data.fstatus,tb_live_data.fsolder_layer,tb_live_data.fweld_bead FROM tb_live_data "
                            + "WHERE (fstatus= '98' OR fstatus= '99')"
                            + " AND tb_live_data.FWeldTime BETWEEN '" + timealarm + "' AND '" + time2 + "' "
                            + "GROUP BY tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fjunction_id,tb_live_data.fstatus,tb_live_data.fmachine_id,tb_live_data.fsolder_layer,tb_live_data.fweld_bead";

                    stmt.executeUpdate(sqlstandby);
                    stmt.executeUpdate(sqlwork);
                    stmt.executeUpdate(sqlwarn);
                    stmt.executeUpdate(sqlalarm);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, time, 1000 * 60 * 60);

        //获取最新焊口和焊机统计时间
        check = new DB_Connectioncode(stmt, conn, connet);
        nettyServerHandler.websocket.dbdata = this.dbdata;

        listarray1 = check.getId1();
        listarray2 = check.getId2();
        listarray3 = check.getId3();

        //System.out.println(listarray1);
        //System.out.println(listarray2);
        //System.out.println(listarray3);

        nettyServerHandler.mysql.listarray1 = this.listarray1;
        nettyServerHandler.mysql.listarray2 = this.listarray2;
        nettyServerHandler.mysql.listarray3 = this.listarray3;
        nettyServerHandler.websocket.listarray1 = this.listarray1;
        nettyServerHandler.websocket.listarray2 = this.listarray2;
        nettyServerHandler.websocket.listarray3 = this.listarray3;
        nettyServerHandler.android.listarray1 = this.listarray1;
        nettyServerHandler.android.listarray2 = this.listarray2;
        nettyServerHandler.listarray1 = this.listarray1;
        nettyServerHandler.listarray2 = this.listarray2;
        nettyServerHandler.listarray3 = this.listarray3;

        //开启线程每分钟更新焊口数据
        Timer tExit2 = null;
        tExit2 = new Timer();
        tExit2.schedule(new TimerTask() {
            @Override
            public void run() {

                try {
                    if (stmt == null || stmt.isClosed() || !conn.isValid(1)) {
                        try {
                            Class.forName("com.mysql.cj.jdbc.Driver");
                            conn = DriverManager.getConnection(connet);
                            stmt = conn.createStatement();
                        } catch (Exception e) {
                            return;
                        }
                    }

                    check = new DB_Connectioncode(stmt, conn, connet);

                    listarray1 = check.getId1();
                    listarray2 = check.getId2();
                    listarray3 = check.getId3();

                    nettyServerHandler.mysql.listarray1 = listarray1;
                    nettyServerHandler.mysql.listarray2 = listarray2;
                    nettyServerHandler.mysql.listarray3 = listarray3;
                    nettyServerHandler.android.listarray1 = listarray1;
                    nettyServerHandler.android.listarray2 = listarray2;
                    nettyServerHandler.listarray1 = listarray1;
                    nettyServerHandler.listarray2 = listarray2;
                    nettyServerHandler.listarray3 = listarray3;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }, 0, 6000000);
        System.out.println("定时任务启动成功！");
    }

    //开启5551端口获取焊机数据
    public Runnable socketstart = new Runnable() {
        @Override
        public void run() {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG, 1024)
                        .option(ChannelOption.SO_KEEPALIVE, true);
                //.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
                //b.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
                b = b.childHandler(new ChannelInitializer<SocketChannel>() { // (4)
                    @Override
                    public void initChannel(SocketChannel chsoc) throws Exception {
                        synchronized (socketlist) {
                            chsoc.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                            chsoc.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
                            chsoc.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
                            chsoc.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                            chsoc.pipeline().addLast(
                                    new ReadTimeoutHandler(100),
                                    new WriteTimeoutHandler(100),
                                    nettyServerHandler);
                            socketcount++;
                            socketlist.put(Integer.toString(socketcount), chsoc);
                            nettyServerHandler.socketlist = socketlist;
                            NWS.socketlist = socketlist;
                            client.handler.socketlist = socketlist;
                            mqtt.mqttReceriveCallback.socketlist = socketlist;
                        }
                    }
                });
                //绑定端口，等待同步成功
                ChannelFuture f = b.bind(5551).sync();
                if (f.isSuccess()) {
                    System.out.println("PC服务端启动成功，监听端口：" + 5551);
                }
                //等待服务端关闭监听端口
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("PC服务端启动异常");
            } finally {
                //释放线程池资源
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
                System.out.println("PC服务端已关闭！");
            }
        }
    };

    //mqtt连接
    private Runnable mqttConnect = new Runnable() {
        @Override
        public void run() {
            mqtt.init("mqttclient");
            nettyServerHandler.mqtt = mqtt;
            nettyServerHandler.websocket.mqtt = mqtt;
            mqtt.subTopic("weldmes/downparams");
        }
    };

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
//        Thread desktopServerThread = new Thread(new Server());
//        desktopServerThread.start();
        new Server();
    }

}


