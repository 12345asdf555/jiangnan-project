/*
 * MainFrame.java
 *
 * Created on 2016.8.19
 */

package com.yang.serialport.ui;

import com.alibaba.fastjson.JSONArray;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import net.sf.json.JSONObject;
import org.apache.axis2.AxisFault;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.datacontract.schemas._2004._07.jn_weld_service.CompositeType;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.tempuri.WeldServiceStub;
import service.weld.jn.ServiceCall;
import service.weld.jn.ServiceCallResponse;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.xml.namespace.QName;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.*;
import java.util.Timer;
import java.util.Map.Entry;

public class MainFrame extends JFrame {

    public Server server;
    public String fitemid;
    public NettyServerHandler nettyServerHandler = new NettyServerHandler();
    public NettyServerHandlerF NSF = new NettyServerHandlerF();
    public NettyClientHandler nettyClientHandler = new NettyClientHandler(nettyServerHandler, NSF, this);
    public TcpClientHandler TC = new TcpClientHandler();
    public HashMap<String, SocketChannel> socketlist = new HashMap();
    public int socketcount = 0;
    public WeldServiceStub stu = null;
    public WeldServiceStub stu1 = null;
    public HashMap<String, String> hm;
    public int wpscount = 1;
    public boolean timersign = true;
    public static final String ipConfigPath = "OTCcomJN/IPconfig.txt";
    //public static final String ipConfigPath = "IPconfig.txt";

    /**
     * 程序界面宽度
     */
    public static final int WIDTH = 500;

    /**
     * 程序界面高度
     */
    public static final int HEIGHT = 360;

    public JTextArea dataView = new JTextArea();
    private JScrollPane scrollDataView = new JScrollPane(dataView);

    // 串口设置面板
    private JPanel serialPortPanel = new JPanel();
    private JLabel baudrateLabel = new JLabel("波特率");
    private JComboBox commChoice = new JComboBox();
    private JComboBox baudrateChoice = new JComboBox();

    // 操作面板
    private JPanel operatePanel = new JPanel();
    private JButton serialPortOperate = new JButton("停止接收");
    private JButton sendData = new JButton("开始接收");

    private String ip;
    public ArrayList<String> listarrayJN = new ArrayList<String>();
    private IsnullUtil iutil;
    private JaxWsDynamicClientFactory dcf;
    private Client client;
    public static boolean iffirst = true;
    public Timer pantimer;

    public static void main(String args[]) {
        new MainFrame().setVisible(true);
    }

    public MainFrame() {
        if (iffirst) {
            //开启定时任务更新焊机时间和获取焊工焊机任务等信息
            scheduleTimerTask();
            //开启OTC服务端供焊机链接
            new Thread(work).start();
            //开启OTC客户端连接PC服务端
            new Thread(cli).start();
            iffirst = false;
        }
    }

    public void scheduleTimerTask() {
        //获取服务器IP地址以及组织机构号
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
                } else if (writetime == 1) {
                    fitemid = line;
                    writetime++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fitemid.length() != 2) {
            int count = 2 - fitemid.length();
            for (int i = 0; i < count; i++) {
                fitemid = "0" + fitemid;
            }
        }
        nettyServerHandler.fitemid = fitemid;

        //加载界面布局
        initView();
        initComponents();

        //webservice配置
        iutil = new IsnullUtil();
        dcf = JaxWsDynamicClientFactory.newInstance();
        client = dcf.createClient("http://" + ip + ":8080/CIWJN_Service/cIWJNWebService?wsdl");
        //client = dcf.createClient("http://10.38.3.30:8080/CIWJN_Service/cIWJNWebService?wsdl");
        iutil.Authority(client);

        Calendar calendarmail = Calendar.getInstance();
        calendarmail.add(Calendar.DAY_OF_MONTH, 1);
        calendarmail.set(Calendar.HOUR_OF_DAY, 6); // 控制时
        calendarmail.set(Calendar.MINUTE, 00);    // 控制分
        calendarmail.set(Calendar.SECOND, 00);    // 控制秒
        Date time = calendarmail.getTime();
        Timer tExit2 = new Timer();
        tExit2.schedule(new TimerTask() {
            @Override
            public void run() {
                StaticClass.listarrayJN.clear();
            }
        }, time, 86400000);

        Timer tExit3 = new Timer();
        tExit3.schedule(new TimerTask() {
            @Override
            public void run() {
                ser();
            }
        }, 3600000, 3600000);

        //功能实现线程
        Timer tExit1 = null;
        tExit1 = new Timer();
        tExit1.schedule(new TimerTask() {
            private String socketfail;

            @Override
            public void run() {
                try {
                    ser();
                    //十分钟发送时间校准
                    Calendar d = Calendar.getInstance();
                    String year = Integer.toString(d.get(Calendar.YEAR));
                    String month = Integer.toString(d.get(Calendar.MONTH) + 1);
                    int length2 = 2 - month.length();
                    if (length2 != 2) {
                        for (int i = 0; i < length2; i++) {
                            month = "0" + month;
                        }
                    }
                    String day = Integer.toString(d.get(Calendar.DAY_OF_MONTH));
                    int length3 = 2 - day.length();
                    if (length3 != 2) {
                        for (int i = 0; i < length3; i++) {
                            day = "0" + day;
                        }
                    }
                    String hour = Integer.toString(d.get(Calendar.HOUR_OF_DAY));
                    int length4 = 2 - hour.length();
                    if (length4 != 2) {
                        for (int i = 0; i < length4; i++) {
                            hour = "0" + hour;
                        }
                    }
                    String minutes = Integer.toString(d.get(Calendar.MINUTE));
                    int length5 = 2 - minutes.length();
                    if (length5 != 2) {
                        for (int i = 0; i < length5; i++) {
                            minutes = "0" + minutes;
                        }
                    }
                    String seconds = Integer.toString(d.get(Calendar.SECOND));
                    int length6 = 2 - seconds.length();
                    if (length6 != 2) {
                        for (int i = 0; i < length6; i++) {
                            seconds = "0" + seconds;
                        }
                    }
                    String time = "7E0F010101450000" + year + month + day + hour + minutes + seconds + "007D";
                    synchronized (socketlist) {
                        //System.out.println("socketlist.size:"+socketlist.size());
                        //System.out.println("socketlist:"+socketlist);

                        ArrayList<String> listarraybuf = new ArrayList<String>();
                        boolean ifdo = false;

                        Iterator<Entry<String, SocketChannel>> webiter = socketlist.entrySet().iterator();
                        while (webiter.hasNext()) {
                            try {

                                Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
                                socketfail = entry.getKey();
                                SocketChannel socketcon = entry.getValue();

                                byte[] b = new byte[time.length() / 2];

                                for (int i = 0; i < time.length(); i += 2) {
                                    if (i <= 14 || i >= 30) {
                                        String buf = time.substring(0 + i, 2 + i);
                                        b[i / 2] = (byte) Integer.parseInt(buf, 16);
                                    } else {
                                        String buf = time.substring(0 + i, 2 + i);
                                        b[i / 2] = (byte) Integer.parseInt(buf);
                                    }
                                }

                                ByteBuf byteBuf = Unpooled.buffer();
                                byteBuf.writeBytes(b);

                                try {
                                    if (socketcon.isOpen() && socketcon.isActive() && socketcon.isWritable()) {
                                        socketcon.writeAndFlush(byteBuf).sync();
                                    } else {
                                        listarraybuf.add(socketfail);
                                        ifdo = true;
                                    }

                                } catch (Exception e) {
                                    listarraybuf.add(socketfail);
                                    ifdo = true;
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("数据解析异常：" + e);
                            }
                        }

                        //clientconnectTest.mainFrame.DateView(str);

                        if (ifdo) {
                            for (int i = 0; i < listarraybuf.size(); i++) {
                                socketlist.remove(listarraybuf.get(i));
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 60000, 3600000);
        ser();
        nettyServerHandler.dataView = this.dataView;
    }

    //webservice获取焊工、焊机(采集模块)、任务(包括下发任务)对应id值
    private void ser() {
        try {
            //任务下发
            String obj1111 = "{\"CLASSNAME\":\"junctionWebServiceImpl\",\"METHOD\":\"getWeldedJunctionAll\"}";
            Object[] objects = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterNoParamWs"),
                    new Object[]{obj1111});
            String restr = objects[0].toString();
            JSONArray ary = JSONArray.parseArray(restr);
            ArrayList<String> listarraybuf = new ArrayList<String>();
            synchronized (listarrayJN) {
                for (int i = 0; i < ary.size(); i++) {
                    String str = ary.getString(i);
                    JSONObject js = JSONObject.fromObject(str);
                    if (js.getString("OPERATESTATUS").equals("1")) {
                        listarraybuf.add(js.getString("ID"));
                    }
                }
                if (listarraybuf.size() == 0) {
                    for (int i = 0; i < ary.size(); i++) {
                        String str = ary.getString(i);
                        JSONObject js = JSONObject.fromObject(str);
                        if (js.getString("OPERATESTATUS").equals("0") || js.getString("OPERATESTATUS").equals("2")) {
                            if (listarrayJN.size() == 0) {
                                listarrayJN.add(js.getString("ID"));
                                listarrayJN.add(js.getString("REWELDERID"));
                                listarrayJN.add(js.getString("MACHINEID"));
                                listarrayJN.add(js.getString("OPERATESTATUS"));
                                listarrayJN.add(js.getString("MACHINENO"));
                            } else {
                                int count = 0;
                                for (int i1 = 0; i1 < listarrayJN.size(); i1 += 5) {
                                    if (!listarrayJN.get(i1).equals(js.getString("ID"))) {
                                        count++;
                                        if (count == listarrayJN.size() / 5) {
                                            listarrayJN.add(js.getString("ID"));
                                            listarrayJN.add(js.getString("REWELDERID"));
                                            listarrayJN.add(js.getString("MACHINEID"));
                                            listarrayJN.add(js.getString("OPERATESTATUS"));
                                            listarrayJN.add(js.getString("MACHINENO"));
                                            break;
                                        }
                                    } else if (listarrayJN.get(i1 + 3).equals("0") && js.getString("OPERATESTATUS").equals("0")) {
                                        listarrayJN.add(js.getString("ID"));
                                        listarrayJN.add(js.getString("REWELDERID"));
                                        listarrayJN.add(js.getString("MACHINEID"));
                                        listarrayJN.add(js.getString("OPERATESTATUS"));
                                        listarrayJN.add(js.getString("MACHINENO"));
                                        break;
                                    } else if (listarrayJN.get(i1 + 3).equals("0") && js.getString("OPERATESTATUS").equals("2")) {
                                        for (int j = 0; j < 5; j++) {
                                            listarrayJN.remove(i1);
                                        }
                                        listarrayJN.add(js.getString("ID"));
                                        listarrayJN.add(js.getString("REWELDERID"));
                                        listarrayJN.add(js.getString("MACHINEID"));
                                        listarrayJN.add(js.getString("OPERATESTATUS"));
                                        listarrayJN.add(js.getString("MACHINENO"));
                                        break;
                                    }
                                }
                            }
                        }
                    }
                } else if (listarraybuf.size() != 0) {
                    for (int i = 0; i < ary.size(); i++) {
                        String str = ary.getString(i);
                        JSONObject js = JSONObject.fromObject(str);

                        int count1 = 0;
                        for (int l = 0; l < listarraybuf.size(); l++) {
                            if (listarraybuf.get(l).equals(js.getString("ID"))) {
                                break;
                            } else {
                                count1++;
                                if (count1 == listarraybuf.size()) {
                                    if (js.getString("OPERATESTATUS").equals("0") || js.getString("OPERATESTATUS").equals("2")) {
                                        if (listarrayJN.size() == 0) {
                                            listarrayJN.add(js.getString("ID"));
                                            listarrayJN.add(js.getString("REWELDERID"));
                                            listarrayJN.add(js.getString("MACHINEID"));
                                            listarrayJN.add(js.getString("OPERATESTATUS"));
                                            listarrayJN.add(js.getString("MACHINENO"));
                                        } else {
                                            int count = 0;
                                            for (int i1 = 0; i1 < listarrayJN.size(); i1 += 5) {
                                                if (!listarrayJN.get(i1).equals(js.getString("ID"))) {
                                                    count++;
                                                    if (count == listarrayJN.size() / 5) {
                                                        listarrayJN.add(js.getString("ID"));
                                                        listarrayJN.add(js.getString("REWELDERID"));
                                                        listarrayJN.add(js.getString("MACHINEID"));
                                                        listarrayJN.add(js.getString("OPERATESTATUS"));
                                                        listarrayJN.add(js.getString("MACHINENO"));
                                                        break;
                                                    }
                                                } else if (listarrayJN.get(i1 + 3).equals("0") && js.getString("OPERATESTATUS").equals("0")) {
                                                    listarrayJN.add(js.getString("ID"));
                                                    listarrayJN.add(js.getString("REWELDERID"));
                                                    listarrayJN.add(js.getString("MACHINEID"));
                                                    listarrayJN.add(js.getString("OPERATESTATUS"));
                                                    listarrayJN.add(js.getString("MACHINENO"));
                                                    break;
                                                } else if (listarrayJN.get(i1 + 3).equals("0") && js.getString("OPERATESTATUS").equals("2")) {
                                                    for (int j = 0; j < 5; j++) {
                                                        listarrayJN.remove(i1);
                                                    }
                                                    listarrayJN.add(js.getString("ID"));
                                                    listarrayJN.add(js.getString("REWELDERID"));
                                                    listarrayJN.add(js.getString("MACHINEID"));
                                                    listarrayJN.add(js.getString("OPERATESTATUS"));
                                                    listarrayJN.add(js.getString("MACHINENO"));
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                }

                StaticClass.listarrayJN = listarrayJN;
            }

            //任务编号对应
            String obj1 = "{\"CLASSNAME\":\"junctionWebServiceImpl\",\"METHOD\":\"getWeldedJunctionAll\"}";
            Object[] objects1 = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterNoParamWs"),
                    new Object[]{obj1});
            String restr1 = objects1[0].toString();
            JSONArray ary1 = JSONArray.parseArray(restr1);
            ArrayList<String> listjunction = new ArrayList<String>();
            for (int i = 0; i < ary1.size(); i++) {
                String str = ary1.getString(i);
                JSONObject js = JSONObject.fromObject(str);
                listjunction.add(js.getString("ID"));
                listjunction.add(js.getString("TASKNO"));
            }
            StaticClass.listjunction = listjunction;

            //焊工
            String obj11 = "{\"CLASSNAME\":\"welderWebServiceImpl\",\"METHOD\":\"getWelderAll\"}";
            String obj22 = "{\"STR\":\"\"}";
            Object[] objects11 = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterTheWS"),
                    new Object[]{obj11, obj22});
            String restr11 = objects11[0].toString();
            JSONArray ary11 = JSONArray.parseArray(restr11);
            ArrayList<String> listwelder = new ArrayList<String>();
            for (int i = 0; i < ary11.size(); i++) {
                String str = ary11.getString(i);
                JSONObject js = JSONObject.fromObject(str);
                listwelder.add(js.getString("WELDERID"));
                listwelder.add(js.getString("WELDERNO"));
            }
            StaticClass.listwelder = listwelder;

            //焊机
            String obj111 = "{\"CLASSNAME\":\"weldingMachineWebServiceImpl\",\"METHOD\":\"getGatherMachine\"}";
            Object[] objects111 = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterNoParamWs"),
                    new Object[]{obj111});
            String restr111 = objects111[0].toString();
            JSONArray ary111 = JSONArray.parseArray(restr111);
            ArrayList<String> listweld = new ArrayList<String>();
            for (int i = 0; i < ary111.size(); i++) {
                String str = ary111.getString(i);
                JSONObject js = JSONObject.fromObject(str);
                listweld.add(js.getString("GATHERID"));//采集id
                listweld.add(js.getString("GATHERNO"));//采集编号
                listweld.add(js.getString("MACHINEID"));//焊机id
                listweld.add(js.getString("MACHINENO"));//焊机编号
            }
            StaticClass.listweld = listweld;

            System.out.println("listarrayJN:" + StaticClass.listarrayJN);
            //System.out.println("listweld:" + StaticClass.listweld);
            //System.out.println("listwelder:" + StaticClass.listwelder);
            //System.out.println("listjunction:" + StaticClass.listjunction);
            System.out.println("socketlist:" + StaticClass.socketlist);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            dataView.append("Webservice未开启" + "\r\n");
            e.printStackTrace();
        }
    }

    //界面布局
    public void initView() {
        // 关闭程序
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        // 禁止窗口最大化
        setResizable(false);

        // 设置程序窗口居中显示
        Point p = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getCenterPoint();
        setBounds(p.x - WIDTH / 2, p.y - HEIGHT / 2, WIDTH, HEIGHT);
        this.setLayout(null);

        setTitle("Wifi采集器");
    }

    //界面布局
    public void initComponents() {
        // 数据显示
        //dataView.setFocusable(false);
        dataView.setEditable(false);
        dataView.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        if (dataView.getLineCount() >= 1000) {
                            int end = 0;
                            try {
                                end = dataView.getLineEndOffset(500);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dataView.replaceRange("", 0, end);
                        }
                    }
                });
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        scrollDataView.setBounds(10, 10, 475, 300);
        add(scrollDataView);

        commChoice.setFocusable(false);
        commChoice.setBounds(60, 25, 100, 20);
        serialPortPanel.add(commChoice);

        baudrateLabel.setForeground(Color.gray);
        baudrateLabel.setBounds(10, 60, 40, 20);
        serialPortPanel.add(baudrateLabel);

        baudrateChoice.setFocusable(false);
        baudrateChoice.setBounds(60, 60, 100, 20);
        serialPortPanel.add(baudrateChoice);

        // 操作
        operatePanel.setBorder(BorderFactory.createTitledBorder("操作"));
        operatePanel.setBounds(70, 220, 375, 100);
        operatePanel.setLayout(null);

        serialPortOperate.setFocusable(false);
        serialPortOperate.setBounds(210, 40, 90, 30);

        sendData.setFocusable(false);
        sendData.setBounds(70, 40, 90, 30);
    }

    public void DateView(String datesend) {
        dataView.append(datesend + "\r\n");
    }

    //开启服务器供焊机连接
    private Runnable work = new Runnable() {
        public void run() {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG, 1024)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);
                b = b.childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel chsoc) throws Exception {
                        synchronized (socketlist) {
                            //编码解码
                            chsoc.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 1, 1, 0, 0));
                            chsoc.pipeline().addLast("frameEncoder", new LengthFieldPrepender(1));
                            //加入编码解码之后,不能加入utf-8编码,加入之后0x80之后的数错误
                            //chsoc.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
                            //chsoc.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));

                            //焊机连接上后,存入list数组中
                            chsoc.pipeline().addLast(nettyServerHandler);
                            socketcount++;
                            socketlist.put(Integer.toString(socketcount), chsoc);
                            TC.socketlist = socketlist;
                            StaticClass.socketlist = socketlist;
                        }
                    }
                });
                //绑定端口，等待同步成功
                ChannelFuture f = b.bind(5555).sync();
                if (f.isSuccess()) {
                    System.out.println("OTC服务端启动成功,监听端口是：" + 5555);
                }
                //等待服务端关闭监听端口
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                //释放线程池资源
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
                System.out.println("OTC服务端已关闭！");
            }
        }
    };
    //OTC采集器连接PC服务器
    private Runnable cli = new Runnable() {
        @Override
        public void run() {
            try {
                nettyClientHandler.run();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };


    //松下实时数据获取
    public Runnable pan = new Runnable() {
        @Override
        public void run() {
            String ip = "";
            try {
                FileInputStream in = new FileInputStream(ipConfigPath);
                InputStreamReader inReader = new InputStreamReader(in, "UTF-8");
                BufferedReader bufReader = new BufferedReader(inReader);
                String line = null;
                int writetime = 0;
                while ((line = bufReader.readLine()) != null) {
                    if (writetime == 0) {
                        writetime++;
                    } else if (writetime == 1) {
                        writetime++;
                    } else if (writetime == 2) {
                        ip = line;
                        writetime++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String[] ipbuf = ip.split(":");
            if (ipbuf.length != 1) {
                try {
                    stu = new WeldServiceStub("http://" + ipbuf[1] + ":8734/JN_WELD_Service/Service1/");
                    //stu = new WeldServiceStub("http://10.30.8.20:8734/JN_WELD_Service/Service1/");
                    stu._getServiceClient().getOptions().setProperty(HTTPConstants.REUSE_HTTP_CLIENT, true);
                    stu._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED, "false");//设置不受限制
                    StaticClass.stu = stu;

                    stu1 = new WeldServiceStub("http://" + ipbuf[1] + ":8735/JN_WELD_Service/Service1/");
                    //stu1 = new WeldServiceStub("http://10.30.8.20:8735/JN_WELD_Service/Service1/");
                    stu1._getServiceClient().getOptions().setProperty(HTTPConstants.REUSE_HTTP_CLIENT, true);
                    stu1._getServiceClient().getOptions().setProperty(HTTPConstants.CHUNKED, "false");//设置不受限制
                    StaticClass.stu1 = stu1;
                } catch (AxisFault e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                hm = weldwps(stu1);
                pantimer = new Timer();
                pantimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (timersign) {
                            wpscount++;
                            if (wpscount == 60) {
                                hm = weldwps(stu1);
                                wpscount = 1;
                            }
                            nettyServerHandler.tranpan(stu1, hm);
                        }
                    }
                }, 2000, 2000);
            }
        }

        //松下获取wpf规范
        private HashMap<String, String> weldwps(WeldServiceStub stu) {
            // TODO Auto-generated method stub
            HashMap<String, String> tmphm = new HashMap<String, String>();

            try {
                ServiceCall sc = new ServiceCall();
                CompositeType tt = new CompositeType();
                tt.setWeldDataTable("");
                tt.setCmdCode(603220102);
                sc.setCmd(tt);

                ServiceCallResponse a = stu.serviceCall(sc);
                CompositeType rs = a.getServiceCallResult();
                String xml = rs.getWeldDataTable();

                Document doc = DocumentHelper.parseText(xml);
                Element rootElt = doc.getRootElement(); // 获取根节点
                List nodes = rootElt.elements("dt");
                //hm.clear();
                for (Iterator it = nodes.iterator(); it.hasNext(); ) {
                    Element elm = (Element) it.next();

                    String nom = Integer.toHexString(Integer.valueOf(elm.element("nom").getStringValue())); //设备编号
                    if (nom.length() < 4) {
                        int len = 4 - nom.length();
                        for (int i = 0; i < len; i++) {
                            nom = "0" + nom;
                        }
                    }

                    String channel = Integer.toHexString(Integer.valueOf(elm.element("channel").getStringValue())); //通道
                    if (channel.length() < 2) {
                        int len = 2 - channel.length();
                        for (int i = 0; i < len; i++) {
                            channel = "0" + channel;
                        }
                    }

                    String wa_up = Integer.toString(Integer.valueOf(elm.element("wa_up").getStringValue())); //报警电流上限
                    if (wa_up.length() < 4) {
                        int len = 4 - wa_up.length();
                        for (int i = 0; i < len; i++) {
                            wa_up = "0" + wa_up;
                        }
                    }

                    String wa_down = Integer.toString(Integer.valueOf(elm.element("wa_down").getStringValue())); //报警电流下限
                    if (wa_down.length() < 4) {
                        int len = 4 - wa_down.length();
                        for (int i = 0; i < len; i++) {
                            wa_down = "0" + wa_down;
                        }
                    }

                    String wv_up = Integer.toString((int) (Double.valueOf(elm.element("wv_up").getStringValue()) * 10)); //报警电压上限
                    if (wv_up.length() < 4) {
                        int len = 4 - wv_up.length();
                        for (int i = 0; i < len; i++) {
                            wv_up = "0" + wv_up;
                        }
                    }

                    String wv_down = Integer.toString((int) (Double.valueOf(elm.element("wv_down").getStringValue()) * 10)); //报警电压下限
                    if (wv_down.length() < 4) {
                        int len = 4 - wv_down.length();
                        for (int i = 0; i < len; i++) {
                            wv_down = "0" + wv_down;
                        }
                    }

                    String va_up = Integer.toString(Integer.valueOf(elm.element("va_up").getStringValue())); //电流上限
                    String va_down = Integer.toString(Integer.valueOf(elm.element("va_down").getStringValue())); //电流下限
                    String vv_up = Integer.toString((int) (Double.valueOf(elm.element("vv_up").getStringValue()) * 10)); //电压上限
                    String vv_down = Integer.toString((int) (Double.valueOf(elm.element("vv_down").getStringValue()) * 10)); //电压下限

                    tmphm.put(nom + ":" + channel, wa_up + "," + wa_down + "," + wv_up + "," + wv_down + "," + va_up + "," + va_down + "," + vv_up + "," + vv_down);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return tmphm;
        }
    };

    //点击事件关闭界面
    private void closeSerialPort(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    //福尼斯测试
    public Runnable workf = new Runnable() {
        public void run() {
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup)
                        .channel(NioServerSocketChannel.class)
                        .option(ChannelOption.SO_BACKLOG, 10240);
                b = b.childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel chsoc) throws Exception {
                        synchronized (socketlist) {
                            //编码解码
                            chsoc.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(10240, 0, 4, 0, 4));
                            chsoc.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
                            //加入编码解码之后,不能加入utf-8编码,加入之后0x80之后的数错误
                            //chsoc.pipeline().addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
                            //chsoc.pipeline().addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));

                            //焊机连接上后,存入list数组中
                            chsoc.pipeline().addLast(
                                    new ReadTimeoutHandler(100),
                                    new WriteTimeoutHandler(100),
                                    NSF);
                            socketlist.clear();
                            socketcount++;
                            socketlist.put(Integer.toString(socketcount), chsoc);
                            StaticClass.socketlist = socketlist;
                            //TC.socketlist = socketlist;
                        }
                    }
                }).option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);

                //绑定端口，等待同步成功
                ChannelFuture f;
                f = b.bind(5554).sync();
                //等待服务端关闭监听端口
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                //释放线程池资源
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }
    };

    public Runnable ser = new Runnable() {
        @Override
        public void run() {
            //任务webservice
            try {

                try {
                    FileInputStream in = new FileInputStream(ipConfigPath);
                    InputStreamReader inReader = new InputStreamReader(in, "UTF-8");
                    BufferedReader bufReader = new BufferedReader(inReader);
                    String line = null;
                    int writetime = 0;

                    while ((line = bufReader.readLine()) != null) {
                        if (writetime == 0) {
                            ip = line;
                            writetime++;
                        }
                    }

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                //任务下发
				/*iutil  =  new IsnullUtil();
				dcf = JaxWsDynamicClientFactory.newInstance();
				//client = dcf.createClient("http://" + ip + ":8080/CIWJN_Service/cIWJNWebService?wsdl");
				client = dcf.createClient("http://" + ip + ":8080/CIWJN_Service/cIWJNWebService?wsdl");
				iutil.Authority(client);

				String obj1 = "{\"CLASSNAME\":\"junctionWebServiceImpl\",\"METHOD\":\"getWeldedJunctionAll\"}";
				Object[] objects = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterNoParamWs"),
						new Object[] { obj1 });
				String restr = objects[0].toString();
		        JSONArray ary = JSONArray.parseArray(restr);

		        ArrayList<String> listarraybuf = new ArrayList<String>();

		        synchronized(listarrayJN){
		        for(int i=0;i<ary.size();i++){
			        String str = ary.getString(i);
			        JSONObject js = JSONObject.fromObject(str);

			        if(js.getString("OPERATESTATUS").equals("1")){
		        		listarraybuf.add(js.getString("ID"));
		        	}else{

		        		int count1=0;
		        		for(int l=0;l<listarraybuf.size();l++){
		        			if(listarraybuf.get(l).equals(js.getString("ID"))){
		        				break;
		        			}else{
		        				count1++;
		        				if(count1==listarraybuf.size()){
			        				if(js.getString("OPERATESTATUS").equals("0") || js.getString("OPERATESTATUS").equals("2")){
			    			        	listarrayJN.add(js.getString("ID"));
			    			        	listarrayJN.add(js.getString("REWELDERID"));
			    			        	listarrayJN.add(js.getString("MACHINEID"));
			    			        	listarrayJN.add(js.getString("OPERATESTATUS"));
			    			        	listarrayJN.add(js.getString("MACHINENO"));
			    			        }
		        				}
		        			}
		        		}
		        	}
		        }
		        NS.listarrayJN = listarrayJN;
		        }*/

                //任务编号对应
                iutil = new IsnullUtil();
                dcf = JaxWsDynamicClientFactory.newInstance();
                //client = dcf.createClient("http://" + ip + ":8080/CIWJN_Service/cIWJNWebService?wsdl");
                client = dcf.createClient("http://" + ip + ":8080/CIWJN_Service/cIWJNWebService?wsdl");
                iutil.Authority(client);
                String obj1 = "{\"CLASSNAME\":\"junctionWebServiceImpl\",\"METHOD\":\"getWeldedJunctionAll\"}";
                Object[] objects1 = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterNoParamWs"),
                        new Object[]{obj1});
                String restr1 = objects1[0].toString();
                JSONArray ary1 = JSONArray.parseArray(restr1);
                ArrayList<String> listjunction = new ArrayList<String>();

                //焊工
                String obj11 = "{\"CLASSNAME\":\"welderWebServiceImpl\",\"METHOD\":\"getWelderAll\"}";
                String obj22 = "{\"STR\":\"\"}";
                Object[] objects11 = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterTheWS"),
                        new Object[]{obj11, obj22});
                String restr11 = objects11[0].toString();
                JSONArray ary11 = JSONArray.parseArray(restr11);
                ArrayList<String> listwelder = new ArrayList<String>();

                //焊机
                String obj111 = "{\"CLASSNAME\":\"weldingMachineWebServiceImpl\",\"METHOD\":\"getGatherMachine\"}";
                Object[] objects111 = client.invoke(new QName("http://webservice.ssmcxf.sshome.com/", "enterNoParamWs"),
                        new Object[]{obj111});
                String restr111 = objects111[0].toString();
                JSONArray ary111 = JSONArray.parseArray(restr111);
                ArrayList<String> listweld = new ArrayList<String>();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                dataView.append("Webservice未开启" + "\r\n");
                e.printStackTrace();
            }
        }
    };
}  

