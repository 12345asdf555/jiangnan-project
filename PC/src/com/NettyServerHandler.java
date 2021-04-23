package com;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.ReferenceCountUtil;

import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * 接收OTC采集器发过来的数据并处理
 */
@Sharable
public class NettyServerHandler extends ChannelHandlerAdapter {

    public static SocketChannel socketchannel = null;
    public static ArrayList<String> listarray1 = new ArrayList<String>();
    public static ArrayList<String> listarray2 = new ArrayList<String>();
    public static ArrayList<String> listarray3 = new ArrayList<String>();
    public static HashMap<String, SocketChannel> socketlist = new HashMap<>();
    public static HashMap<String, SocketChannel> websocketlist = new HashMap<>();
    public static Mysql mysql = new Mysql();
    public static Android android = new Android();
    public static Websocket websocket = new Websocket();
    public static MyMqttClient mqtt;

    /**
     * 读取通道中的消息
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String str = "";
        try {
            str = (String) msg;
            workSpace(str);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
            ReferenceCountUtil.release(str);
            ctx.flush();
        }
    }

    private void workSpace(String str) {
        String socketfail = "";
        if (str.length() == 284 || str.length() == 124) {
            if ("7E".equals(str.substring(0, 2)) && "22".equals(str.substring(10, 12))){
                //数据存入数据库
                mysql.Mysqlbase(str);
                //发送前端
                websocket.Websocketbase(str, listarray2, listarray3, websocketlist);
            }
        } else if ("FA".equals(str.substring(0, 2)) && str.length() == 110) {  //处理实时数据
//            mysql.Mysqlrun(str);
//            websocket.Websocketrun(str, listarray2, listarray3, websocketlist);
//            if (socketchannel != null) {
//                try {
//                    socketchannel.writeAndFlush(str).sync();
//                } catch (Exception e) {
//                    socketchannel = null;
//                }
//            }
        } else if (str.substring(0, 2).equals("þ")) {   //处理android数据
            //android.Androidrun(str);
        } else if (str.substring(0, 2).equals("JN")) {  //江南任务派发 任务号、焊工、焊机、状态
            String[] datainf = str.split(",");
            String datasend = "";
            String junction = "";
            String cengdao = "";
            String gather = "";
            int cengdaocount = 0;
            Connection connection = null;
            Statement statement = null;
            try {
                connection = MysqlDBConnection.getConnection();
                statement = connection.createStatement();
                String inSql = "SELECT tb_welded_junction.fwelded_junction_no,tb_specification.fsolder_layer,tb_specification.fweld_bead FROM tb_welded_junction INNER JOIN tb_specification ON tb_welded_junction.fwpslib_id = tb_specification.fwpslib_id WHERE tb_welded_junction.fid = '" + datainf[1] + "' ORDER BY tb_specification.fweld_bead asc";
                ResultSet rs = statement.executeQuery(inSql);
                while (rs.next()) {
                    junction = rs.getString("tb_welded_junction.fwelded_junction_no");
                    String ceng = Integer.toString(Integer.valueOf(rs.getString("tb_specification.fsolder_layer"), 16));//焊层
                    if (ceng.length() != 2) {
                        ceng = "0" + ceng;
                    }
                    String dao = Integer.toString(Integer.valueOf(rs.getString("tb_specification.fweld_bead"), 16));//焊道
                    if (dao.length() != 2) {
                        dao = "0" + dao;
                    }
                    cengdao = cengdao + ceng + dao;
                    cengdaocount++;
                }

                String inSql1 = "SELECT fgather_no FROM tb_gather INNER JOIN tb_welding_machine ON tb_gather.fid = tb_welding_machine.fgather_id WHERE tb_welding_machine.fid = '" + datainf[3] + "'";
                ResultSet rs1 = statement.executeQuery(inSql1);
                while (rs1.next()) {
                    gather = Integer.toString(Integer.valueOf(rs.getString("fgather_no"), 16));
                    if (gather.length() != 4) {
                        for (int i = 0; i < 4 - gather.length(); i++) {
                            gather = "0" + gather;
                        }
                    }
                }
            } catch (Exception e) {
                e.getStackTrace();
            } finally {
                //释放连接，归还资源
                MysqlDBConnection.close(connection, statement, null);
            }

            if (cengdao.length() != 100) {
                for (int i = 0; i < 100 - cengdao.length(); i++) {
                    cengdao = cengdao + "0";
                }
            }

            if (junction.length() != 30) {
                for (int i = 0; i < 30 - junction.length(); i++) {
                    cengdao = cengdao + "0";
                }
            }

            String cdcount = Integer.toString(cengdaocount);
            if (cdcount.length() != 4) {
                for (int i = 0; i < 4 - cdcount.length(); i++) {
                    cdcount = cdcount + "0";
                }
            }

            if (datainf[4].equals("0")) {
                datasend = "7E0001010122" + gather + "00" + junction + cengdao + cdcount + "017D";
            } else if (datainf[4].equals("1")) {
                datasend = "7E0001010122" + gather + "01" + junction + cengdao + cdcount + "017D";
            }

            ArrayList<String> listarraybuf = new ArrayList<String>();
            boolean ifdo = false;
            HashMap<String, SocketChannel> socketlist_cl;
            Iterator<Entry<String, SocketChannel>> webiter = socketlist.entrySet().iterator();
            while (webiter.hasNext()) {
                try {
                    Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
                    socketfail = entry.getKey();
                    SocketChannel socketcon = entry.getValue();
                    if (socketcon.isOpen() && socketcon.isActive() && socketcon.isWritable()) {
                        socketcon.writeAndFlush(str);
                        socketcon.writeAndFlush(datasend);
                    } else {
                        listarraybuf.add(socketfail);
                        ifdo = true;
                    }

                } catch (Exception e) {
                    listarraybuf.add(socketfail);
                    ifdo = true;
                    e.getStackTrace();
                }
            }
            if (ifdo) {
                //socketlist_cl = (HashMap<String, SocketChannel>) socketlist.clone();
                for (int i = 0; i < listarraybuf.size(); i++) {
                    //socketlist.remove(listarraybuf.get(i));
                }
            }

        } else if (str.length() == 38 && str.substring(10, 12).equals("01")) {    //处理焊层焊道信息
            mysql.db.ceng = Integer.valueOf(str.substring(18, 20), 16);
            mysql.db.dao = Integer.valueOf(str.substring(20, 22), 16);
            mysql.db.weldstatus = Integer.valueOf(str.substring(16, 18), 16);
            if (socketchannel != null) {
                try {
                    socketchannel.writeAndFlush(str).sync();
                } catch (Exception e) {
                    socketchannel = null;
                    //e.printStackTrace();
                }
            }
        } else if (str.substring(0, 2).equals("fe") && str.substring(str.length() - 2, str.length()).equals("fe")) {  //华域PLC
            ////System.out.println("1");
            //System.out.println("接收数据:"+str);
//            if (socketchannel != null) {
//                ////System.out.println(socketchannel);
//                synchronized (socketchannel) {
//                    try {
//                        socketchannel.writeAndFlush(str).sync();
//                        //System.out.println("发送成功:"+str);
//                    } catch (Exception e) {
//                        try {
//                            socketchannel.close().sync();
//                        } catch (InterruptedException e1) {
//                            // TODO Auto-generated catch block
//                            e1.printStackTrace();
//                        }
//                        socketchannel = null;
//                        //e.printStackTrace();
//                    }
//                }
//            }
        } else {    //处理焊机下发和上传
//            System.out.println("mqtt.publishMessage:"+str);
            MyMqttClient.publishMessage("weldmes/upparams", str, 0);
        }
    }

    /**
     * @param ctx
     * @throws Exception
     * @description: 有客户端连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        int clientPort = insocket.getPort();
        // 唯一标识
        ChannelId channelId = ctx.channel().id();
        System.out.println("新增连接:" + clientIp + "：" + clientPort);
    }

    /**
     * @param ctx
     * @description: 有客户端终止连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        int port = insocket.getPort();
        ChannelId channelId = ctx.channel().id();
        System.out.println("终止连接:" + clientIp + "：" + port);
        //ctx.channel().close();
        //ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}
