package com;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Email {
    private Date time;

    public void run() {
        //开启线程每天查询邮件
        Calendar calendarmail = Calendar.getInstance();

        calendarmail.set(Calendar.HOUR_OF_DAY, 17); // 控制时
        calendarmail.set(Calendar.MINUTE, 50);    // 控制分
        calendarmail.set(Calendar.SECOND, 00);    // 控制秒
        time = calendarmail.getTime();

        Timer tExit3 = null;
        tExit3 = new Timer();
        tExit3.schedule(new TimerTask() {
            Connection connection = null;
            Statement stmt = null;

            @Override
            public void run() {
                try {
                    //获取焊工以及管理员信息
                    connection = MysqlDBConnection.getConnection();
                    stmt = connection.createStatement();

                    ArrayList<String> listarraymail = new ArrayList<String>();
                    ArrayList<String> listarraymailer = new ArrayList<String>();
                    String sqlmail = "SELECT tb_catweldinf.fweldername,tb_catweldinf.fcheckintime,tb_catweldinf.ficworkime FROM tb_catweldinf";
                    String sqlmailer = "SELECT femailname,femailaddress,femailtype FROM tb_catemailinf";
                    ResultSet rs;
                    try {
                        rs = stmt.executeQuery(sqlmail);
                        while (rs.next()) {
                            listarraymail.add(rs.getString("fweldername"));
                            listarraymail.add(rs.getString("fcheckintime"));
                            listarraymail.add(rs.getString("ficworkime"));
                        }
                        rs = stmt.executeQuery(sqlmailer);
                        while (rs.next()) {
                            listarraymailer.add(rs.getString("femailname"));
                            listarraymailer.add(rs.getString("femailaddress"));
                            listarraymailer.add(rs.getString("femailtype"));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    String halfyearname = "";

                    for (int i = 0; i < listarraymail.size(); i += 3) {

                        //半年提醒
                        Calendar canow = Calendar.getInstance();
                        Calendar ca = Calendar.getInstance();
                        ca.setTime(new Date());
                        ca.add(Calendar.MONTH, -5);
                        ca.add(Calendar.DAY_OF_MONTH, -15);
                        Date resultDate = ca.getTime(); // 结果
                        String nowtime = DateTools.format("yyyy-MM-dd HH:mm:ss", resultDate);

                        String[] nowtimebuf = nowtime.split(" ");
                        String[] checkintimebuf = listarraymail.get(i + 1).split(" ");

                        nowtime = nowtimebuf[0];
                        String checkintime = checkintimebuf[0];

                        if (nowtime.equals(checkintime)) {
                            if (halfyearname.equals("")) {
                                halfyearname = listarraymail.get(i);
                            } else {
                                halfyearname = listarraymail.get(i) + "、" + halfyearname;
                            }

                            String sqlmailcheck2 = "update tb_catweldinf set fhalfyearsure = '" + DateTools.format("yyyy-MM-dd HH:mm:ss", new Date()) + "' WHERE fweldername = '" + listarraymail.get(i) + "'";
                            try {
                                stmt.execute(sqlmailcheck2);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    if (!halfyearname.equals("")) {
                        try {

                            for (int j = 0; j < listarraymailer.size(); j += 3) {
                                if (listarraymailer.get(j + 2).equals("1")) {
                                    Properties props = new Properties();
                                    props.setProperty("mail.smtp.auth", "true");
                                    props.setProperty("mail.transport.protocol", "smtp");
                                    props.put("mail.smtp.host", "smtp.qq.com");// smtp服务器地址

                                    Session session = Session.getInstance(props);
                                    session.setDebug(true);

                                    Message msg = new MimeMessage(session);
                                    msg.setSubject("员工入职半年提醒");
                                    msg.setText(halfyearname + " 入职已满半年");
                                    msg.setSentDate(new Date());
                                    msg.setFrom(new InternetAddress("512836904@qq.com"));//发件人邮箱
                                    msg.setRecipient(Message.RecipientType.TO,
                                            new InternetAddress(listarraymailer.get(j + 1))); //收件人邮箱
                                    //msg.addRecipient(Message.RecipientType.CC,
                                    //new InternetAddress("XXXXXXXXXXX@qq.com")); //抄送人邮箱
                                    msg.saveChanges();

                                    Transport transport = session.getTransport();
                                    transport.connect("512836904@qq.com", "sbmqftbsitpecaef");//发件人邮箱,授权码

                                    transport.sendMessage(msg, msg.getAllRecipients());
                                    transport.close();

                                    String nowtime = DateTools.format("yyyy-MM-dd HH:mm:ss", new Date());
                                    String sqlmailcheck1 = "INSERT INTO tb_catemailcheck (femailname, femailaddress, femailtext, femailstatus, femailtime) VALUES ('" + listarraymailer.get(j) + "' , '" + listarraymailer.get(j + 1) + "' , '" + halfyearname + " 入职已满半年" + "' , '1' , '" + nowtime + "')";
                                    stmt.execute(sqlmailcheck1);
                                }
                            }

                        } catch (Exception e) {
                            e.getStackTrace();
                        }
                    }

                    //ic卡有效期提醒
                    String icworktime = "";

                    for (int i = 0; i < listarraymail.size(); i += 3) {

                        //ic卡有效期提醒

                        Date dateic;
                        try {
                            dateic = DateTools.parse("yyyy-MM-dd HH:mm:ss", listarraymail.get(i + 2));
                            Calendar canow = Calendar.getInstance();
                            Calendar ca = Calendar.getInstance();
                            ca.setTime(dateic);
                            ca.add(Calendar.DAY_OF_MONTH, -60);
                            Date resultDate = ca.getTime(); // 结果
                            String ictime = DateTools.format("yyyy-MM-dd HH:mm:ss", resultDate);

                            String[] timebuf = ictime.split(" ");
                            String[] checkictimebuf = DateTools.format("yyyy-MM-dd HH:mm:ss", canow.getTime()).split(" ");

                            ictime = timebuf[0];
                            String checkictime = checkictimebuf[0];

                            if (ictime.equals(checkictime)) {
                                if (icworktime.equals("")) {
                                    icworktime = listarraymail.get(i);
                                } else {
                                    icworktime = listarraymail.get(i) + "、" + icworktime;
                                }
                            }
                        } catch (java.text.ParseException e) {
                            // TODO Auto-generated catch block
                            //e.printStackTrace();
                        }
                    }

                    if (!icworktime.equals("")) {
                        try {

                            for (int j = 0; j < listarraymailer.size(); j += 3) {
                                if (listarraymailer.get(j + 2).equals("2")) {
                                    Properties props = new Properties();
                                    props.setProperty("mail.smtp.auth", "true");
                                    props.setProperty("mail.transport.protocol", "smtp");
                                    props.put("mail.smtp.host", "smtp.qq.com");// smtp服务器地址

                                    Session session = Session.getInstance(props);
                                    session.setDebug(true);

                                    Message msg = new MimeMessage(session);
                                    msg.setSubject("员工ic卡到期提醒");
                                    msg.setText(icworktime + " ic卡将要过期");
                                    msg.setSentDate(new Date());
                                    msg.setFrom(new InternetAddress("512836904@qq.com"));//发件人邮箱
                                    msg.setRecipient(Message.RecipientType.TO,
                                            new InternetAddress(listarraymailer.get(j + 1))); //收件人邮箱
                                    //msg.addRecipient(Message.RecipientType.CC,
                                    //new InternetAddress("XXXXXXXXXXX@qq.com")); //抄送人邮箱
                                    msg.saveChanges();

                                    Transport transport = session.getTransport();
                                    transport.connect("512836904@qq.com", "sbmqftbsitpecaef");//发件人邮箱,授权码

                                    transport.sendMessage(msg, msg.getAllRecipients());
                                    transport.close();

                                    String nowtime = DateTools.format("yyyy-MM-dd HH:mm:ss", new Date());
                                    String sqlmailcheck = "INSERT INTO tb_catemailcheck (femailname, femailaddress, femailtext, femailstatus, femailtime) VALUES ('" + listarraymailer.get(j) + "' , '" + listarraymailer.get(j + 1) + "' , '" + icworktime + " ic卡将要过期" + "' , '2' , '" + nowtime + "')";
                                    stmt.execute(sqlmailcheck);
                                }
                            }

                        } catch (Exception e) {
                            e.getStackTrace();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    MysqlDBConnection.close(connection, stmt, null);
                }
            }
        }, time, 86400000);
    }
}
