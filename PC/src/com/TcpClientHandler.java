package com;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

@Sharable
public class TcpClientHandler extends SimpleChannelInboundHandler {

    public static Client client;
    public HashMap<String, SocketChannel> socketlist = new HashMap<>();
    private String socketfail;

    @Override
    protected void messageReceived(ChannelHandlerContext arg0, Object arg1) throws Exception {
        // TODO Auto-generated method stub
        String str = (String) arg1;

        if (str.substring(0, 2).equals("JN")) {  //江南任务派发 任务号、焊工、焊机、状态
            String[] datainf = str.split(",");

            String junctionsend = "";
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
                    String ceng = Integer.toHexString(Integer.valueOf(rs.getString("tb_specification.fsolder_layer")));
                    if (ceng.length() != 2) {
                        ceng = "0" + ceng;
                    }
                    String dao = Integer.toHexString(Integer.valueOf(rs.getString("tb_specification.fweld_bead")));
                    if (dao.length() != 2) {
                        dao = "0" + dao;
                    }
                    cengdao = cengdao + ceng + dao;
                    cengdaocount++;
                }

                String inSql1 = "SELECT fgather_no FROM tb_gather INNER JOIN tb_welding_machine ON tb_gather.fid = tb_welding_machine.fgather_id WHERE tb_welding_machine.fid = '" + datainf[3] + "'";
                ResultSet rs1 = statement.executeQuery(inSql1);
                while (rs1.next()) {
                    gather = Integer.toHexString(Integer.valueOf(rs1.getString("fgather_no")));
                    if (gather.length() != 4) {
                        for (int i = 0; i < 4 - gather.length(); i++) {
                            gather = "0" + gather;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                MysqlDBConnection.close(connection,statement,null);
            }


            if (cengdao.length() != 200) {
                int count = cengdao.length();
                for (int i = 0; i < 200 - count; i++) {
                    cengdao = cengdao + "0";
                }
            }

            if (gather.length() != 4) {
                int count = gather.length();
                for (int i = 0; i < 4 - count; i++) {
                    gather = "0" + gather;
                }
            }

            String cdcount = Integer.toHexString(cengdaocount);
            if (cdcount.length() != 4) {
                int count = cdcount.length();
                for (int i = 0; i < 4 - count; i++) {
                    cdcount = "0" + cdcount;
                }
            }

            char[] buf = junction.toCharArray();
            for (int i = 0; i < buf.length; i++) {
                int buf1 = buf[i];
                junctionsend = junctionsend + Integer.toString(buf1, 16);
            }


            if (junctionsend.length() != 60) {
                int count = junctionsend.length();
                for (int i = 0; i < 60 - count; i++) {
                    junctionsend = junctionsend + "0";
                }
            }

            if (datainf[4].equals("0")) {
                datasend = "7E8D01010102" + gather + "00" + junctionsend + cengdao + cdcount + "017D";
            } else if (datainf[4].equals("1")) {
                DB_Connectionmysql.ceng = 0;
                DB_Connectionmysql.dao = 0;
                datasend = "7E8D01010102" + gather + "01" + junctionsend + cengdao + cdcount + "017D";
            }

            synchronized (socketlist) {
                ArrayList<String> listarraybuf = new ArrayList<String>();
                boolean ifdo = false;

                Iterator<Entry<String, SocketChannel>> iter = socketlist.entrySet().iterator();
                while (iter.hasNext()) {
                    try {
                        Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) iter.next();

                        //System.out.println(entry);

                        socketfail = entry.getKey();

                        SocketChannel socketcon = entry.getValue();
                        socketcon.writeAndFlush(str).sync();
                        socketcon.writeAndFlush(datasend).sync();

                    } catch (Exception e) {
                        //e.printStackTrace();
                        listarraybuf.add(socketfail);
                        ifdo = true;
                    }
                }

                if (ifdo) {
                    for (int i = 0; i < listarraybuf.size(); i++) {
                        socketlist.remove(listarraybuf.get(i));
                    }
                }
            }


        } else if (str.substring(0, 12).equals("7E3501010152") || str.substring(0, 10).equals("FE5AA5006e")) {

            synchronized (socketlist) {
                ArrayList<String> listarraybuf = new ArrayList<String>();
                boolean ifdo = false;

                Iterator<Entry<String, SocketChannel>> webiter = socketlist.entrySet().iterator();
                while (webiter.hasNext()) {
                    try {
                        Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
                        socketfail = entry.getKey();
                        SocketChannel socketcon = entry.getValue();
                        socketcon.writeAndFlush(str).sync();
                    } catch (Exception e) {
                        listarraybuf.add(socketfail);
                        ifdo = true;
                    }
                }

                if (ifdo) {
                    for (int i = 0; i < listarraybuf.size(); i++) {
                        socketlist.remove(listarraybuf.get(i));
                    }
                }
            }

        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
        ctx.close();
        ctx.disconnect();
        ctx.flush();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        final EventLoop eventLoop = ctx.channel().eventLoop();
        eventLoop.schedule(new Runnable() {
            @Override
            public void run() {
                client.createBootstrap(new Bootstrap(), eventLoop);
            }
        }, 1L, TimeUnit.SECONDS);
        super.channelInactive(ctx);
    }

}
