package com;

import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.net.Socket;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.regex.Pattern;

public class Websocket {

    Timestamp timesql1;
    Timestamp timesql2;
    Timestamp timesql3;
    private String limit;
    private String connet;
    public String strsend = "";
    public String strsendpan = "";
    private String strdata;
    private SocketChannel chweb;
    private String websocketfail;
    public ArrayList<String> listarray1;
    public ArrayList<String> listarray2;
    public ArrayList<String> listarray3;
    private boolean datawritetype = false;
    private HashMap<String, Socket> websocket;
    private HashMap<String, SocketChannel> websocketlist = null;
    public ArrayList<String> dbdata = new ArrayList<String>();
    public int count = 0;
    public static MyMqttClient mqtt;
    //判断是否为数字
    public static boolean isInteger(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    public void Websocketbase(String str, ArrayList<String> listarray2, ArrayList<String> listarray3, HashMap<String, SocketChannel> websocketlist) {
        Date time;
        Timestamp timesql = null;

        if (!(websocketlist == null || websocketlist.isEmpty())) {
        } else {
            if (str.length() == 284) {
                //校验第一位是否为FA末位是否为F5
                String check1 = str.substring(0, 2);
                String check11 = str.substring(282, 284);
                if (check1.equals("7E") && check11.equals("7D")) {

                    String welderid = Integer.valueOf(str.substring(34, 38)).toString();
                    if (welderid.length() != 4) {
                        int lenth = 4 - welderid.length();
                        for (int i = 0; i < lenth; i++) {
                            welderid = "0" + welderid;
                        }
                    }
                    String weldid = "0000";
                    if (isInteger(str.substring(18, 22))){
                        weldid = Integer.valueOf(str.substring(18, 22)).toString();
                    }
                    if (weldid.length() != 4) {
                        int lenth = 4 - weldid.length();
                        for (int i = 0; i < lenth; i++) {
                            weldid = "0" + weldid;
                        }
                    }
                    String gatherid = "0000";
                    if (isInteger(str.substring(14, 18))){
                        gatherid = Integer.valueOf(str.substring(14, 18)).toString();//采集id
                    }
                    if (gatherid.length() != 4) {
                        int lenth = 4 - gatherid.length();
                        for (int i = 0; i < lenth; i++) {
                            gatherid = "0" + gatherid;
                        }
                    }
                    String itemins = Integer.valueOf(str.substring(280, 282)).toString();
                    if (itemins.length() != 4) {
                        int lenth = 4 - itemins.length();
                        for (int i = 0; i < lenth; i++) {
                            itemins = "0" + itemins;
                        }
                    }
                    String weldmodel = Integer.valueOf(str.substring(12, 14), 16).toString();
                    if (weldmodel.length() != 4) {
                        int lenth = 4 - weldmodel.length();
                        for (int i = 0; i < lenth; i++) {
                            weldmodel = "0" + weldmodel;
                        }
                    }

                    for (int a = 0; a < 161; a += 80) {

                        String welderins = "0000";
                        String junctionins = "0000";
                        String ins = "0000";

                        String junctionid = "0000";
                        if (isInteger(str.substring(70 + a, 78 + a))){
                            junctionid = Integer.valueOf(str.substring(70 + a, 78 + a)).toString();
                        } else {
                            junctionid = Integer.valueOf(str.substring(70 + a, 78 + a),16).toString();
                        }
                        if (junctionid.length() <= 4) {
                            int lenth = 4 - junctionid.length();
                            for (int i = 0; i < lenth; i++) {
                                junctionid = "0" + junctionid;
                            }
                        } else {
                            junctionid = "0000";
                        }
                        String electricity = Integer.valueOf(str.substring(50 + a, 54 + a), 16).toString();
                        if (electricity.length() != 4) {
                            int lenth = 4 - electricity.length();
                            for (int i = 0; i < lenth; i++) {
                                electricity = "0" + electricity;
                            }
                        }
                        String voltage = Integer.valueOf(str.substring(54 + a, 58 + a), 16).toString();
                        if (voltage.length() != 4) {
                            int lenth = 4 - voltage.length();
                            for (int i = 0; i < lenth; i++) {
                                voltage = "0" + voltage;
                            }
                        }
                        String setelectricity = Integer.valueOf(str.substring(62 + a, 66 + a), 16).toString();
                        if (setelectricity.length() != 4) {
                            int lenth = 4 - setelectricity.length();
                            for (int i = 0; i < lenth; i++) {
                                setelectricity = "0" + setelectricity;
                            }
                        }
                        String setvoltage = Integer.valueOf(str.substring(66 + a, 70 + a), 16).toString();
                        if (setvoltage.length() != 4) {
                            int lenth = 4 - setvoltage.length();
                            for (int i = 0; i < lenth; i++) {
                                setvoltage = "0" + setvoltage;
                            }
                        }
                        String status = Integer.valueOf(str.substring(78 + a, 80 + a), 16).toString();
                        if (status.length() != 2) {
                            int lenth = 2 - status.length();
                            for (int i = 0; i < lenth; i++) {
                                status = "0" + status;
                            }
                        }

                        String year = Integer.valueOf(str.substring(38 + a, 40 + a), 16).toString();
                        String month = Integer.valueOf(str.substring(40 + a, 42 + a), 16).toString();
                        String day = Integer.valueOf(str.substring(42 + a, 44 + a), 16).toString();
                        String hour = Integer.valueOf(str.substring(44 + a, 46 + a), 16).toString();
                        String minute = Integer.valueOf(str.substring(46 + a, 48 + a), 16).toString();
                        String second = Integer.valueOf(str.substring(48 + a, 50 + a), 16).toString();
                        String strdate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                        try {
                            time = DateTools.parse("yy-MM-dd HH:mm:ss", strdate);
                            timesql = new Timestamp(time.getTime());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        String channel = Integer.valueOf(str.substring(100 + a, 102 + a), 16).toString();
                        if (channel.length() != 4) {
                            int lenth = 4 - channel.length();
                            for (int i = 0; i < lenth; i++) {
                                channel = "0" + channel;
                            }
                        }

                        //焊接电流电压上下限
                        String maxelectricity = Integer.toString(Integer.valueOf(str.substring(84 + a, 88 + a), 16).intValue() + Integer.valueOf(str.substring(92 + a, 94 + a).toString(), 16).intValue());
                        if (maxelectricity.length() != 4) {
                            int lenth = 4 - maxelectricity.length();
                            for (int i = 0; i < lenth; i++) {
                                maxelectricity = "0" + maxelectricity;
                            }
                        }
                        String minelectricity = null;
                        if (((Integer.valueOf(str.substring(84 + a, 88 + a).toString(), 16).intValue()) - (Integer.valueOf(str.substring(92 + a, 94 + a).toString(), 16).intValue())) < 0) {
                            minelectricity = "0000";
                        } else {
                            minelectricity = Integer.toString((Integer.valueOf(str.substring(84 + a, 88 + a).toString(), 16).intValue()) - (Integer.valueOf(str.substring(92 + a, 94 + a).toString(), 16).intValue()));
                        }
                        //String minelectricity = Integer.toString((Integer.valueOf(str.substring(84+a, 88+a).toString(),16).intValue())-(Integer.valueOf(str.substring(92+a, 94+a).toString(),16).intValue()));
                        if (minelectricity.length() != 4) {
                            int lenth = 4 - minelectricity.length();
                            for (int i = 0; i < lenth; i++) {
                                minelectricity = "0" + minelectricity;
                            }
                        }
                        String maxvoltage = Integer.toString((Integer.valueOf(str.substring(88 + a, 92 + a).toString(), 16).intValue()) + (Integer.valueOf(str.substring(94 + a, 96 + a).toString(), 16).intValue()));
                        if (maxvoltage.length() != 4) {
                            int lenth = 4 - maxvoltage.length();
                            for (int i = 0; i < lenth; i++) {
                                maxvoltage = "0" + maxvoltage;
                            }
                        }
                        String minvoltage = null;
                        if (((Integer.valueOf(str.substring(88 + a, 92 + a).toString(), 16).intValue()) - (Integer.valueOf(str.substring(94 + a, 96 + a).toString(), 16).intValue())) < 0) {
                            minvoltage = "0000";
                        } else {
                            minvoltage = Integer.toString((Integer.valueOf(str.substring(88 + a, 92 + a).toString(), 16).intValue()) - (Integer.valueOf(str.substring(94 + a, 96 + a).toString(), 16).intValue()));
                        }
                        //String minvoltage = Integer.toString((Integer.valueOf(str.substring(88+a, 92+a).toString(),16).intValue())-(Integer.valueOf(str.substring(94+a, 96+a).toString(),16).intValue()));
                        if (minvoltage.length() != 4) {
                            int lenth = 4 - minvoltage.length();
                            for (int i = 0; i < lenth; i++) {
                                minvoltage = "0" + minvoltage;
                            }
                        }

                        //报警电流电压上下限wmaxvoltage
                        String wmaxelectricity = Integer.toString(Integer.valueOf(str.substring(102 + a, 106 + a).toString(), 16).intValue());
                        if (wmaxelectricity.length() != 4) {
                            int lenth = 4 - wmaxelectricity.length();
                            for (int i = 0; i < lenth; i++) {
                                wmaxelectricity = "0" + wmaxelectricity;
                            }
                        }
                        String wmaxvoltage = Integer.toString(Integer.valueOf(str.substring(106 + a, 110 + a).toString(), 16).intValue());
                        if (wmaxvoltage.length() != 4) {
                            int lenth = 4 - wmaxvoltage.length();
                            for (int i = 0; i < lenth; i++) {
                                wmaxvoltage = "0" + wmaxvoltage;
                            }
                        }
                        String wminelectricity = Integer.toString(Integer.valueOf(str.substring(110 + a, 114 + a).toString(), 16).intValue());
                        if (wminelectricity.length() != 4) {
                            int lenth = 4 - wminelectricity.length();
                            for (int i = 0; i < lenth; i++) {
                                wminelectricity = "0" + wminelectricity;
                            }
                        }
                        String wminvoltage = Integer.toString(Integer.valueOf(str.substring(114 + a, 118 + a).toString(), 16).intValue());
                        if (wminvoltage.length() != 4) {
                            int lenth = 4 - wminvoltage.length();
                            for (int i = 0; i < lenth; i++) {
                                wminvoltage = "0" + wminvoltage;
                            }
                        }

                        for (int i = 0; i < listarray1.size(); i += 3) {
                            if (Integer.valueOf(welderid) == Integer.valueOf(listarray1.get(i))) {
                                welderins = listarray1.get(i + 2);
                                if (welderins.equals(null) || welderins.equals("null")) {
                                    break;
                                } else {
                                    if (welderins.length() != 4) {
                                        int lenth = 4 - welderins.length();
                                        for (int i1 = 0; i1 < lenth; i1++) {
                                            welderins = "0" + welderins;
                                        }
                                    }
                                    break;
                                }
                            }
                        }

                        for (int i = 0; i < listarray3.size(); i += 7) {
                            if (Integer.valueOf(junctionid) == Integer.valueOf(listarray3.get(i + 5))) {
                                junctionins = listarray3.get(i + 6);
                                if (junctionins.equals(null) || junctionins.equals("null")) {
                                    break;
                                } else {
                                    if (junctionins.length() != 4) {
                                        int lenth = 4 - junctionins.length();
                                        for (int i1 = 0; i1 < lenth; i1++) {
                                            junctionins = "0" + junctionins;
                                        }
                                    }
                                    break;
                                }
                            }
                        }

                        for (int i = 0; i < listarray2.size(); i += 4) {
                            if (gatherid.equals(listarray2.get(i))) {
                                ins = listarray2.get(i + 3);
                                if (ins == null || ins.equals("null")) {
                                    break;
                                } else {
                                    if (ins.length() != 4) {
                                        int lenth = 4 - ins.length();
                                        for (int i1 = 0; i1 < lenth; i1++) {
                                            ins = "0" + ins;
                                        }
                                    }
                                    break;
                                }
                            }
                        }

                        if (ins == null || ins.equals("null")) {
                            ins = "0000";
                        }
                        if (junctionins.equals(null) || junctionins.equals("null")) {
                            junctionins = "0000";
                        }
                        if (welderins.equals(null) || welderins.equals("null")) {
                            welderins = "0000";
                        }

                        strsend = strsend + welderid + weldid + gatherid + junctionid + welderins + junctionins + ins + itemins + weldmodel + status + electricity + voltage + setelectricity + setvoltage + timesql + maxelectricity + minelectricity + maxvoltage + minvoltage + channel + wmaxelectricity + wminelectricity + wmaxvoltage + wminvoltage;
                    }
                    mqtt.publishMessage("weldmes/rtcdata", strsend, 0);
                    strsend = "";
                }
            } else if (str.length() == 124) {  //松下
                String check1 = str.substring(0, 2);
                String check11 = str.substring(122, 124);
                if (check1.equals("7E") && check11.equals("7D")) {

                    String welderid = Integer.valueOf(str.substring(34, 38)).toString();
                    if (welderid.length() != 4) {
                        int lenth = 4 - welderid.length();
                        for (int i = 0; i < lenth; i++) {
                            welderid = "0" + welderid;
                        }
                    }
                    String weldid = Integer.valueOf(str.substring(18, 22)).toString();
                    if (weldid.length() != 4) {
                        int lenth = 4 - weldid.length();
                        for (int i = 0; i < lenth; i++) {
                            weldid = "0" + weldid;
                        }
                    }
                    String gatherid = Integer.valueOf(str.substring(14, 18)).toString();
                    if (gatherid.length() != 4) {
                        int lenth = 4 - gatherid.length();
                        for (int i = 0; i < lenth; i++) {
                            gatherid = "0" + gatherid;
                        }
                    }
                    String itemins = Integer.valueOf(str.substring(120, 122)).toString();
                    if (itemins.length() != 4) {
                        int lenth = 4 - itemins.length();
                        for (int i = 0; i < lenth; i++) {
                            itemins = "0" + itemins;
                        }
                    }
                    String weldmodel = Integer.valueOf(str.substring(12, 14).toString(), 16).toString();
                    if (weldmodel.length() != 4) {
                        int lenth = 4 - weldmodel.length();
                        for (int i = 0; i < lenth; i++) {
                            weldmodel = "0" + weldmodel;
                        }
                    }

                    String welderins = "0000";
                    String junctionins = "0000";
                    String ins = "0000";

                    String junctionid = Integer.valueOf(str.substring(70, 78)).toString();
                    if (junctionid.length() != 4) {
                        int lenth = 4 - junctionid.length();
                        for (int i = 0; i < lenth; i++) {
                            junctionid = "0" + junctionid;
                        }
                    }
                    String electricity = Integer.valueOf(str.substring(50, 54).toString(), 16).toString();
                    if (electricity.length() != 4) {
                        int lenth = 4 - electricity.length();
                        for (int i = 0; i < lenth; i++) {
                            electricity = "0" + electricity;
                        }
                    }
                    String voltage = Integer.valueOf(str.substring(54, 58).toString(), 16).toString();
                    if (voltage.length() != 4) {
                        int lenth = 4 - voltage.length();
                        for (int i = 0; i < lenth; i++) {
                            voltage = "0" + voltage;
                        }
                    }
                    String setelectricity = Integer.valueOf(str.substring(62, 66).toString(), 16).toString();
                    if (setelectricity.length() != 4) {
                        int lenth = 4 - setelectricity.length();
                        for (int i = 0; i < lenth; i++) {
                            setelectricity = "0" + setelectricity;
                        }
                    }
                    String setvoltage = Integer.valueOf(str.substring(66, 70).toString(), 16).toString();
                    if (setvoltage.length() != 4) {
                        int lenth = 4 - setvoltage.length();
                        for (int i = 0; i < lenth; i++) {
                            setvoltage = "0" + setvoltage;
                        }
                    }
                    String status = Integer.valueOf(str.substring(78, 80).toString(), 16).toString();
                    if (status.length() != 2) {
                        int lenth = 2 - status.length();
                        for (int i = 0; i < lenth; i++) {
                            status = "0" + status;
                        }
                    }

                    String year = Integer.valueOf(str.substring(38, 40).toString(), 16).toString();
                    String month = Integer.valueOf(str.substring(40, 42).toString(), 16).toString();
                    String day = Integer.valueOf(str.substring(42, 44).toString(), 16).toString();
                    String hour = Integer.valueOf(str.substring(44, 46).toString(), 16).toString();
                    String minute = Integer.valueOf(str.substring(46, 48).toString(), 16).toString();
                    String second = Integer.valueOf(str.substring(48, 50).toString(), 16).toString();
                    String strdate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
                    try {
                        time = DateTools.parse("yy-MM-dd HH:mm:ss", strdate);
                        timesql = new Timestamp(time.getTime());
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        //e.printStackTrace();
                    }

                    String channel = Integer.valueOf(str.substring(100, 102).toString(), 16).toString();
                    if (channel.length() != 4) {
                        int lenth = 4 - channel.length();
                        for (int i = 0; i < lenth; i++) {
                            channel = "0" + channel;
                        }
                    }
                    //焊接电流电压上下限
                    String maxelectricity = Integer.toString(Integer.valueOf(str.substring(84, 88).toString(), 16).intValue() + Integer.valueOf(str.substring(92, 94).toString(), 16).intValue());
                    if (maxelectricity.length() != 4) {
                        int lenth = 4 - maxelectricity.length();
                        for (int i = 0; i < lenth; i++) {
                            maxelectricity = "0" + maxelectricity;
                        }
                    }
                    String minelectricity = null;
                    if (((Integer.valueOf(str.substring(84, 88).toString(), 16).intValue()) - (Integer.valueOf(str.substring(92, 94).toString(), 16).intValue())) < 0) {
                        minelectricity = "0000";
                    } else {
                        minelectricity = Integer.toString((Integer.valueOf(str.substring(84, 88).toString(), 16).intValue()) - (Integer.valueOf(str.substring(92, 94).toString(), 16).intValue()));
                    }
                    //String minelectricity = Integer.toString((Integer.valueOf(str.substring(84+a, 88+a).toString(),16).intValue())-(Integer.valueOf(str.substring(92+a, 94+a).toString(),16).intValue()));
                    if (minelectricity.length() != 4) {
                        int lenth = 4 - minelectricity.length();
                        for (int i = 0; i < lenth; i++) {
                            minelectricity = "0" + minelectricity;
                        }
                    }
                    String maxvoltage = Integer.toString((Integer.valueOf(str.substring(88, 92).toString(), 16).intValue()) + (Integer.valueOf(str.substring(94, 96).toString(), 16).intValue()));
                    if (maxvoltage.length() != 4) {
                        int lenth = 4 - maxvoltage.length();
                        for (int i = 0; i < lenth; i++) {
                            maxvoltage = "0" + maxvoltage;
                        }
                    }
                    String minvoltage = null;
                    if (((Integer.valueOf(str.substring(88, 92).toString(), 16).intValue()) - (Integer.valueOf(str.substring(94, 96).toString(), 16).intValue())) < 0) {
                        minvoltage = "0000";
                    } else {
                        minvoltage = Integer.toString((Integer.valueOf(str.substring(88, 92).toString(), 16).intValue()) - (Integer.valueOf(str.substring(94, 96).toString(), 16).intValue()));
                    }
                    //String minvoltage = Integer.toString((Integer.valueOf(str.substring(88+a, 92+a).toString(),16).intValue())-(Integer.valueOf(str.substring(94+a, 96+a).toString(),16).intValue()));
                    if (minvoltage.length() != 4) {
                        int lenth = 4 - minvoltage.length();
                        for (int i = 0; i < lenth; i++) {
                            minvoltage = "0" + minvoltage;
                        }
                    }

                    //报警电流电压上下限wmaxvoltage
                    String wmaxelectricity = Integer.toString(Integer.valueOf(str.substring(102, 106).toString(), 16).intValue());
                    if (wmaxelectricity.length() != 4) {
                        int lenth = 4 - wmaxelectricity.length();
                        for (int i = 0; i < lenth; i++) {
                            wmaxelectricity = "0" + wmaxelectricity;
                        }
                    }
                    String wmaxvoltage = Integer.toString(Integer.valueOf(str.substring(106, 110).toString(), 16).intValue());
                    if (wmaxvoltage.length() != 4) {
                        int lenth = 4 - wmaxvoltage.length();
                        for (int i = 0; i < lenth; i++) {
                            wmaxvoltage = "0" + wmaxvoltage;
                        }
                    }
                    String wminelectricity = Integer.toString(Integer.valueOf(str.substring(110, 114).toString(), 16).intValue());
                    if (wminelectricity.length() != 4) {
                        int lenth = 4 - wminelectricity.length();
                        for (int i = 0; i < lenth; i++) {
                            wminelectricity = "0" + wminelectricity;
                        }
                    }
                    String wminvoltage = Integer.toString(Integer.valueOf(str.substring(114, 118).toString(), 16).intValue());
                    if (wminvoltage.length() != 4) {
                        int lenth = 4 - wminvoltage.length();
                        for (int i = 0; i < lenth; i++) {
                            wminvoltage = "0" + wminvoltage;
                        }
                    }

                    for (int i = 0; i < listarray1.size(); i += 3) {
                        if (Integer.valueOf(welderid) == Integer.valueOf(listarray1.get(i))) {
                            welderins = listarray1.get(i + 2);
                            if (welderins.equals(null) || welderins.equals("null")) {
                                break;
                            } else {
                                if (welderins.length() != 4) {
                                    int lenth = 4 - welderins.length();
                                    for (int i1 = 0; i1 < lenth; i1++) {
                                        welderins = "0" + welderins;
                                    }
                                }
                                break;
                            }
                        }
                    }

                    for (int i = 0; i < listarray3.size(); i += 7) {
                        if (Integer.valueOf(junctionid) == Integer.valueOf(listarray3.get(i + 5))) {
                            junctionins = listarray3.get(i + 6);
                            if (junctionins.equals(null) || junctionins.equals("null")) {
                                break;
                            } else {
                                if (junctionins.length() != 4) {
                                    int lenth = 4 - junctionins.length();
                                    for (int i1 = 0; i1 < lenth; i1++) {
                                        junctionins = "0" + junctionins;
                                    }
                                }
                                break;
                            }
                        }
                    }

                    for (int i = 0; i < listarray2.size(); i += 4) {
                        if (Integer.valueOf(gatherid) == Integer.valueOf(listarray2.get(i))) {
                            ins = listarray2.get(i + 3);
                            if (ins == null || ins.equals("null")) {
                                break;
                            } else {
                                if (ins.length() != 4) {
                                    int lenth = 4 - ins.length();
                                    for (int i1 = 0; i1 < lenth; i1++) {
                                        ins = "0" + ins;
                                    }
                                }
                                break;
                            }
                        }
                    }

                    if (ins == null || ins.equals("null")) {
                        ins = "0000";
                    }
                    if (junctionins.equals(null) || junctionins.equals("null")) {
                        junctionins = "0000";
                    }
                    if (welderins.equals(null) || welderins.equals("null")) {
                        welderins = "0000";
                    }
                    synchronized (this) {
                        count++;
                    }

                    strsendpan = strsendpan + welderid + weldid + gatherid + junctionid + welderins + junctionins + ins + itemins + weldmodel + status + electricity + voltage + setelectricity + setvoltage + timesql + maxelectricity + minelectricity + maxvoltage + minvoltage + channel + wmaxelectricity + wminelectricity + wmaxvoltage + wminvoltage;

                    mqtt.publishMessage("weldmes/rtcdata", strsendpan, 0);
                    strsendpan = "";
                }
            }
        }
    }


    public void Websocketrun(String str, ArrayList<String> listarray2, ArrayList<String> listarray3, HashMap<String, SocketChannel> websocketlist) {
        // TODO Auto-generated constructor stub

        this.strdata = str;
        ////System.out.println("1:"+str);

        try {

            //鏃犵敤鎴疯繛鎺ユ椂澧炲姞缁熻鐒婃満宸ヤ綔鏃堕棿
            if (websocketlist == null || websocketlist.isEmpty()) {

            } else {
                if (str.length() == 170) {

                    String check1 = str.substring(0, 2);
                    String check11 = str.substring(108, 110);
                    if (check1.equals("FA") && check11.equals("F5")) {

                        int check2 = str.length();

                        String check3 = str.substring(2, 164);
                        String check5 = "";
                        int check4 = 0;
                        for (int i11 = 0; i11 < check3.length() / 2; i11++) {
                            String tstr1 = check3.substring(i11 * 2, i11 * 2 + 2);
                            check4 += Integer.valueOf(tstr1, 16);
                        }
                        if ((Integer.toHexString(check4)).toUpperCase().length() == 2) {
                            check5 = ((Integer.toHexString(check4)).toUpperCase());
                        } else {
                            check5 = ((Integer.toHexString(check4)).toUpperCase()).substring(1, 3);
                        }
                        String check6 = str.substring(164, 166);
                        if (check5.equals(check6)) {

                            ////System.out.println("2");

                            strdata = str;
                            //String weldname = strdata.substring(10,14);
                            int weldname1 = Integer.valueOf(strdata.substring(10, 14).toString(), 16);
                            String weldname = String.valueOf(weldname1);
                            if (weldname.length() != 4) {
                                int lenth = 4 - weldname.length();
                                for (int i = 0; i < lenth; i++) {
                                    weldname = "0" + weldname;
                                }
                            }

                            int welder1 = Integer.valueOf(strdata.substring(14, 18).toString(), 16);
                            String welder = String.valueOf(welder1);
                            if (welder.length() != 4) {
                                int lenth = 4 - welder.length();
                                for (int i = 0; i < lenth; i++) {
                                    welder = "0" + welder;
                                }
                            }

                            //String code = strdata.substring(18,26);
                            long code1 = Integer.valueOf(strdata.substring(18, 26).toString(), 16);
                            String code = String.valueOf(code1);
                            if (code.length() != 8) {
                                int lenth = 8 - code.length();
                                for (int i = 0; i < lenth; i++) {
                                    code = "0" + code;
                                }
                            }

                            int electricity11 = Integer.valueOf(strdata.substring(26, 30).toString(), 16);
                            String electricity1 = String.valueOf(electricity11);
                            if (electricity1.length() != 4) {
                                int lenth = 4 - electricity1.length();
                                for (int i = 0; i < lenth; i++) {
                                    electricity1 = "0" + electricity1;
                                }
                            }

                            int voltage11 = Integer.valueOf(strdata.substring(30, 34).toString(), 16);
                            String voltage1 = String.valueOf(voltage11);
                            if (voltage1.length() != 4) {
                                int lenth = 4 - voltage1.length();
                                for (int i = 0; i < lenth; i++) {
                                    voltage1 = "0" + voltage1;
                                }
                            }

                            String status1 = Integer.valueOf(strdata.substring(38, 40), 16).toString();
                            if (status1.length() != 2) {
                                int lenth = 2 - status1.length();
                                for (int i = 0; i < lenth; i++) {
                                    status1 = "0" + status1;
                                }
                            }


                            int wirefeedrate11 = Integer.valueOf(strdata.substring(40, 44).toString(), 16);
                            String wirefeedrate1 = String.valueOf(wirefeedrate11);
                            if (wirefeedrate1.length() != 4) {
                                int lenth = 4 - wirefeedrate1.length();
                                for (int i = 0; i < lenth; i++) {
                                    wirefeedrate1 = "0" + wirefeedrate1;
                                }
                            }
                            int weldingrate11 = Integer.valueOf(strdata.substring(40, 48).toString(), 16);
                            String weldingrate1 = String.valueOf(weldingrate11);
                            if (weldingrate1.length() != 4) {
                                int lenth = 4 - weldingrate1.length();
                                for (int i = 0; i < lenth; i++) {
                                    weldingrate1 = "0" + weldingrate1;
                                }
                            }
                            int weldheatinput11 = Integer.valueOf(strdata.substring(48, 52).toString(), 16);
                            String weldheatinput1 = String.valueOf(weldheatinput11);
                            if (weldheatinput1.length() != 4) {
                                int lenth = 4 - weldheatinput1.length();
                                for (int i = 0; i < lenth; i++) {
                                    weldheatinput1 = "0" + weldheatinput1;
                                }
                            }
                            int hatwirecurrent11 = Integer.valueOf(strdata.substring(52, 56).toString(), 16);
                            String hatwirecurrent1 = String.valueOf(hatwirecurrent11);
                            if (hatwirecurrent1.length() != 4) {
                                int lenth = 4 - hatwirecurrent1.length();
                                for (int i = 0; i < lenth; i++) {
                                    hatwirecurrent1 = "0" + hatwirecurrent1;
                                }
                            }
                            int vibrafrequency11 = Integer.valueOf(strdata.substring(56, 60).toString(), 16);
                            String vibrafrequency1 = String.valueOf(vibrafrequency11);
                            if (vibrafrequency1.length() != 4) {
                                int lenth = 4 - vibrafrequency1.length();
                                for (int i = 0; i < lenth; i++) {
                                    vibrafrequency1 = "0" + vibrafrequency1;
                                }
                            }


                            long year1 = Integer.valueOf(str.substring(60, 62).toString(), 16);
                            String yearstr1 = String.valueOf(year1);
                            long month1 = Integer.valueOf(str.substring(62, 64).toString(), 16);
                            String monthstr1 = String.valueOf(month1);
                            if (monthstr1.length() != 2) {
                                int lenth = 2 - monthstr1.length();
                                for (int i = 0; i < lenth; i++) {
                                    monthstr1 = "0" + monthstr1;
                                }
                            }
                            long day1 = Integer.valueOf(str.substring(64, 66).toString(), 16);
                            String daystr1 = String.valueOf(day1);
                            if (daystr1.length() != 2) {
                                int lenth = 2 - daystr1.length();
                                for (int i = 0; i < lenth; i++) {
                                    daystr1 = "0" + daystr1;
                                }
                            }
                            long hour1 = Integer.valueOf(str.substring(66, 68).toString(), 16);
                            String hourstr1 = String.valueOf(hour1);
                            if (hourstr1.length() != 2) {
                                int lenth = 2 - hourstr1.length();
                                for (int i = 0; i < lenth; i++) {
                                    hourstr1 = "0" + hourstr1;
                                }
                            }
                            long minute1 = Integer.valueOf(str.substring(68, 70).toString(), 16);
                            String minutestr1 = String.valueOf(minute1);
                            if (minutestr1.length() != 2) {
                                int lenth = 2 - minutestr1.length();
                                for (int i = 0; i < lenth; i++) {
                                    minutestr1 = "0" + minutestr1;
                                }
                            }
                            long second1 = Integer.valueOf(str.substring(70, 72).toString(), 16);
                            String secondstr1 = String.valueOf(second1);
                            if (secondstr1.length() != 2) {
                                int lenth = 2 - secondstr1.length();
                                for (int i = 0; i < lenth; i++) {
                                    secondstr1 = "0" + secondstr1;
                                }
                            }

                            String timestr1 = yearstr1 + "-" + monthstr1 + "-" + daystr1 + " " + hourstr1 + ":" + minutestr1 + ":" + secondstr1;
                            SimpleDateFormat timeshow1 = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                            try {

                                Date time1 = DateTools.parse("yy-MM-dd HH:mm:ss", timestr1);
                                //java.util.Date time4 = timeshow3.parse(timestr3);
                                timesql1 = new Timestamp(time1.getTime());

                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                //e.printStackTrace();
                            }


                            int electricity22 = Integer.valueOf(strdata.substring(72, 76).toString(), 16);
                            String electricity2 = String.valueOf(electricity22);
                            if (electricity2.length() != 4) {
                                int lenth = 4 - electricity2.length();
                                for (int i = 0; i < lenth; i++) {
                                    electricity2 = "0" + electricity2;
                                }
                            }

                            int voltage22 = Integer.valueOf(strdata.substring(76, 80).toString(), 16);
                            String voltage2 = String.valueOf(voltage22);
                            if (voltage2.length() != 4) {
                                int lenth = 4 - voltage2.length();
                                for (int i = 0; i < lenth; i++) {
                                    voltage2 = "0" + voltage2;
                                }
                            }

                            String status2 = Integer.valueOf(strdata.substring(84, 86), 16).toString();
                            if (status2.length() != 2) {
                                int lenth = 2 - status2.length();
                                for (int i = 0; i < lenth; i++) {
                                    status2 = "0" + status2;
                                }
                            }

                            int wirefeedrate22 = Integer.valueOf(strdata.substring(86, 90).toString(), 16);
                            String wirefeedrate2 = String.valueOf(wirefeedrate22);
                            if (wirefeedrate2.length() != 4) {
                                int lenth = 4 - wirefeedrate2.length();
                                for (int i = 0; i < lenth; i++) {
                                    wirefeedrate2 = "0" + wirefeedrate2;
                                }
                            }
                            int weldingrate22 = Integer.valueOf(strdata.substring(90, 94).toString(), 16);
                            String weldingrate2 = String.valueOf(weldingrate22);
                            if (weldingrate2.length() != 4) {
                                int lenth = 4 - weldingrate2.length();
                                for (int i = 0; i < lenth; i++) {
                                    weldingrate2 = "0" + weldingrate2;
                                }
                            }
                            int weldheatinput22 = Integer.valueOf(strdata.substring(94, 98).toString(), 16);
                            String weldheatinput2 = String.valueOf(weldheatinput22);
                            if (weldheatinput2.length() != 4) {
                                int lenth = 4 - weldheatinput2.length();
                                for (int i = 0; i < lenth; i++) {
                                    weldheatinput2 = "0" + weldheatinput2;
                                }
                            }
                            int hatwirecurrent22 = Integer.valueOf(strdata.substring(98, 102).toString(), 16);
                            String hatwirecurrent2 = String.valueOf(hatwirecurrent22);
                            if (hatwirecurrent2.length() != 4) {
                                int lenth = 4 - hatwirecurrent2.length();
                                for (int i = 0; i < lenth; i++) {
                                    hatwirecurrent2 = "0" + hatwirecurrent2;
                                }
                            }
                            int vibrafrequency22 = Integer.valueOf(strdata.substring(102, 106).toString(), 16);
                            String vibrafrequency2 = String.valueOf(vibrafrequency22);
                            if (vibrafrequency2.length() != 4) {
                                int lenth = 4 - vibrafrequency2.length();
                                for (int i = 0; i < lenth; i++) {
                                    vibrafrequency2 = "0" + vibrafrequency2;
                                }
                            }


                            long year2 = Integer.valueOf(str.substring(106, 108).toString(), 16);
                            String yearstr2 = String.valueOf(year2);
                            long month2 = Integer.valueOf(str.substring(108, 110).toString(), 16);
                            String monthstr2 = String.valueOf(month2);
                            if (monthstr2.length() != 2) {
                                int lenth = 2 - monthstr2.length();
                                for (int i = 0; i < lenth; i++) {
                                    monthstr2 = "0" + monthstr2;
                                }
                            }
                            long day2 = Integer.valueOf(str.substring(110, 112).toString(), 16);
                            String daystr2 = String.valueOf(day2);
                            if (daystr2.length() != 2) {
                                int lenth = 2 - daystr2.length();
                                for (int i = 0; i < lenth; i++) {
                                    daystr2 = "0" + daystr2;
                                }
                            }
                            long hour2 = Integer.valueOf(str.substring(112, 114).toString(), 16);
                            String hourstr2 = String.valueOf(hour2);
                            if (hourstr2.length() != 2) {
                                int lenth = 2 - hourstr2.length();
                                for (int i = 0; i < lenth; i++) {
                                    hourstr2 = "0" + hourstr2;
                                }
                            }
                            long minute2 = Integer.valueOf(str.substring(114, 116).toString(), 16);
                            String minutestr2 = String.valueOf(minute2);
                            if (minutestr2.length() != 2) {
                                int lenth = 2 - minutestr2.length();
                                for (int i = 0; i < lenth; i++) {
                                    minutestr2 = "0" + minutestr2;
                                }
                            }
                            long second2 = Integer.valueOf(str.substring(116, 118).toString(), 16);
                            String secondstr2 = String.valueOf(second2);
                            if (secondstr2.length() != 2) {
                                int lenth = 2 - secondstr2.length();
                                for (int i = 0; i < lenth; i++) {
                                    secondstr2 = "0" + secondstr2;
                                }
                            }

                            String timestr2 = yearstr2 + "-" + monthstr2 + "-" + daystr2 + " " + hourstr2 + ":" + minutestr2 + ":" + secondstr2;
                            SimpleDateFormat timeshow2 = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                            try {

                                Date time2 = DateTools.parse("yy-MM-dd HH:mm:ss", timestr2);
                                //java.util.Date time4 = timeshow3.parse(timestr3);
                                timesql2 = new Timestamp(time2.getTime());

                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                //e.printStackTrace();
                            }


                            int electricity33 = Integer.valueOf(strdata.substring(118, 122).toString(), 16);
                            String electricity3 = String.valueOf(electricity33);
                            if (electricity3.length() != 4) {
                                int lenth = 4 - electricity3.length();
                                for (int i = 0; i < lenth; i++) {
                                    electricity3 = "0" + electricity3;
                                }
                            }

                            int voltage33 = Integer.valueOf(strdata.substring(122, 126).toString(), 16);
                            String voltage3 = String.valueOf(voltage33);
                            if (voltage3.length() != 4) {
                                int lenth = 4 - voltage3.length();
                                for (int i = 0; i < lenth; i++) {
                                    voltage3 = "0" + voltage3;
                                }
                            }
                            String status3 = Integer.valueOf(strdata.substring(130, 132), 16).toString();
                            if (status3.length() != 2) {
                                int lenth = 2 - status3.length();
                                for (int i = 0; i < lenth; i++) {
                                    status3 = "0" + status3;
                                }
                            }

                            int wirefeedrate33 = Integer.valueOf(strdata.substring(132, 136).toString(), 16);
                            String wirefeedrate3 = String.valueOf(wirefeedrate33);
                            if (wirefeedrate3.length() != 4) {
                                int lenth = 4 - wirefeedrate3.length();
                                for (int i = 0; i < lenth; i++) {
                                    wirefeedrate3 = "0" + wirefeedrate3;
                                }
                            }
                            int weldingrate33 = Integer.valueOf(strdata.substring(136, 140).toString(), 16);
                            String weldingrate3 = String.valueOf(weldingrate33);
                            if (weldingrate3.length() != 4) {
                                int lenth = 4 - weldingrate3.length();
                                for (int i = 0; i < lenth; i++) {
                                    weldingrate3 = "0" + weldingrate3;
                                }
                            }
                            int weldheatinput33 = Integer.valueOf(strdata.substring(140, 144).toString(), 16);
                            String weldheatinput3 = String.valueOf(weldheatinput33);
                            if (weldheatinput3.length() != 4) {
                                int lenth = 4 - weldheatinput3.length();
                                for (int i = 0; i < lenth; i++) {
                                    weldheatinput3 = "0" + weldheatinput3;
                                }
                            }
                            int hatwirecurrent33 = Integer.valueOf(strdata.substring(144, 148).toString(), 16);
                            String hatwirecurrent3 = String.valueOf(hatwirecurrent33);
                            if (hatwirecurrent3.length() != 4) {
                                int lenth = 4 - hatwirecurrent3.length();
                                for (int i = 0; i < lenth; i++) {
                                    hatwirecurrent3 = "0" + hatwirecurrent3;
                                }
                            }
                            int vibrafrequency33 = Integer.valueOf(strdata.substring(148, 152).toString(), 16);
                            String vibrafrequency3 = String.valueOf(vibrafrequency33);
                            if (vibrafrequency3.length() != 4) {
                                int lenth = 4 - vibrafrequency3.length();
                                for (int i = 0; i < lenth; i++) {
                                    vibrafrequency3 = "0" + vibrafrequency3;
                                }
                            }


                            long year3 = Integer.valueOf(str.substring(152, 154).toString(), 16);
                            String yearstr3 = String.valueOf(year3);
                            long month3 = Integer.valueOf(str.substring(154, 156).toString(), 16);
                            String monthstr3 = String.valueOf(month3);
                            if (monthstr3.length() != 2) {
                                int lenth = 2 - monthstr3.length();
                                for (int i = 0; i < lenth; i++) {
                                    monthstr3 = "0" + monthstr3;
                                }
                            }
                            long day3 = Integer.valueOf(str.substring(156, 158).toString(), 16);
                            String daystr3 = String.valueOf(day3);
                            if (daystr3.length() != 2) {
                                int lenth = 2 - daystr3.length();
                                for (int i = 0; i < lenth; i++) {
                                    daystr3 = "0" + daystr3;
                                }
                            }
                            long hour3 = Integer.valueOf(str.substring(158, 160).toString(), 16);
                            String hourstr3 = String.valueOf(hour3);
                            if (hourstr3.length() != 2) {
                                int lenth = 2 - hourstr3.length();
                                for (int i = 0; i < lenth; i++) {
                                    hourstr3 = "0" + hourstr3;
                                }
                            }
                            long minute3 = Integer.valueOf(str.substring(160, 162).toString(), 16);
                            String minutestr3 = String.valueOf(minute3);
                            if (minutestr3.length() != 2) {
                                int lenth = 2 - minutestr3.length();
                                for (int i = 0; i < lenth; i++) {
                                    minutestr3 = "0" + minutestr3;
                                }
                            }
                            long second3 = Integer.valueOf(str.substring(162, 164).toString(), 16);
                            String secondstr3 = String.valueOf(second3);
                            if (secondstr3.length() != 2) {
                                int lenth = 2 - secondstr3.length();
                                for (int i = 0; i < lenth; i++) {
                                    secondstr3 = "0" + secondstr3;
                                }
                            }

                            String timestr3 = yearstr3 + "-" + monthstr3 + "-" + daystr3 + " " + hourstr3 + ":" + minutestr3 + ":" + secondstr3;
                            SimpleDateFormat timeshow3 = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                            try {
                                Date time3 = DateTools.parse("yy-MM-dd HH:mm:ss", timestr3);
                                //java.util.Date time4 = timeshow3.parse(timestr3);
                                timesql3 = new Timestamp(time3.getTime());
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                //e.printStackTrace();
                            }

                            ////System.out.println("3");

                            try {

                                for (int i = 0; i < listarray3.size(); i += 5) {
                                    String weldjunction = listarray3.get(i);
                                    if (weldjunction.equals(code)) {
                                        String maxe = listarray3.get(i + 1);
                                        String mixe = listarray3.get(i + 2);
                                        String maxv = listarray3.get(i + 3);
                                        String mixv = listarray3.get(i + 4);
                                        limit = maxe + mixe + maxv + mixv;
                                    }
                                }

                                ////System.out.println("4");

                            /*String worktime = "";
                            String totaltime = "";
                            String worktime1 = "";
                            String totaltime1 = "";
                            String workhour1,workminute1,worksecond1;
                            String workhour2,workminute2,worksecond2;*/


                                for (int i = 0; i < listarray2.size(); i += 4) {
                                    String fequipment_no = listarray2.get(i);
                                    String fgather_no = listarray2.get(i + 2);
                                    String finsframework_id = listarray2.get(i + 3);

                                    //System.out.print(fequipment_no+" ");

                                    if (weldname.equals(fgather_no)) {

                                        ////System.out.println("5");

                                    /*for(int j=0;j<dbdata.size();j+=3){
                                        if(dbdata.get(j).equals(fequipment_no)){
                                            if(status1.equals("00")){
                                                worktime=Integer.toString(Integer.valueOf(dbdata.get(j+1)));
                                                totaltime=Integer.toString(Integer.valueOf(dbdata.get(j+2))+3);
                                                dbdata.set(j+2, totaltime);
                                            }else{
                                                worktime=Integer.toString(Integer.valueOf(dbdata.get(j+1))+3);
                                                totaltime=Integer.toString(Integer.valueOf(dbdata.get(j+2))+3);

                                                dbdata.set(j+1, worktime);
                                                dbdata.set(j+2, totaltime);
                                            }
                                            break;
                                        }
                                    }

                                   long hour = (long)Integer.valueOf(worktime)/3600;
                                 if(hour<10){
                                     workhour1 = "0" + String.valueOf(hour) + ":";
                                 }else{
                                     workhour1 = String.valueOf(hour) + ":";
                                 }
                                 long last = (long)Integer.valueOf(worktime)%3600;
                                 long minute = last/60;
                                 if(minute<10){
                                     workminute1 = "0" + String.valueOf(minute) + ":";
                                 }else{
                                     workminute1 = String.valueOf(minute) + ":";
                                 }
                                 long second = last%60;
                                 if(second<10){
                                     worksecond1 = "0" + String.valueOf(second);
                                 }else{
                                     worksecond1 = String.valueOf(second);
                                 }
                                 worktime1 = workhour1 + workminute1 + worksecond1;

                                   long hour4 = (long)Integer.valueOf(totaltime)/3600;
                                   if(hour4<10){
                                       workhour2 = "0" + String.valueOf(hour4) + ":";
                                   }else{
                                       workhour2 = String.valueOf(hour4) + ":";
                                   }
                                   long last4 = (long)Integer.valueOf(totaltime)%3600;
                                   long minute4 = last4/60;
                                   if(minute4<10){
                                       workminute2 = "0" + String.valueOf(minute4) + ":";
                                   }else{
                                       workminute2 = String.valueOf(minute4) + ":";
                                   }
                                   long second4 = last4%60;
                                   if(second4<10){
                                       worksecond2 = "0" + String.valueOf(second4);
                                   }else{
                                       worksecond2 = String.valueOf(second4);
                                   }
                                    totaltime1 = workhour2 + workminute2 + worksecond2;*/

                                        ////System.out.println("6");
                                        try {

                                            if (fequipment_no.length() != 4) {
                                                int lenth = 4 - fequipment_no.length();
                                                for (int i1 = 0; i1 < lenth; i1++) {
                                                    fequipment_no = "0" + fequipment_no;
                                                }
                                            }

                                            if (weldname.equals(fgather_no)) {
                                           /*if(finsframework_id==null || finsframework_id==""){
                                               finsframework_id="nu";
                                               strsend+=status1+finsframework_id+fequipment_no+welder+electricity1+voltage1+wirefeedrate1+weldingrate1+weldheatinput1+hatwirecurrent1+vibrafrequency1+timesql1+"000000000000"+"00:00:00"+"00:00:00"
                                                       +status2+finsframework_id+fequipment_no+welder+electricity2+voltage2+wirefeedrate2+weldingrate2+weldheatinput2+hatwirecurrent2+vibrafrequency2+timesql2+"000000000000"+"00:00:00"+"00:00:00"
                                                       +status3+finsframework_id+fequipment_no+welder+electricity3+voltage3+wirefeedrate3+weldingrate3+weldheatinput3+hatwirecurrent3+vibrafrequency3+timesql3+"000000000000"+"00:00:00"+"00:00:00";
                                           }else{
                                               strsend+=status1+finsframework_id+fequipment_no+welder+electricity1+voltage1+wirefeedrate1+weldingrate1+weldheatinput1+hatwirecurrent1+vibrafrequency1+timesql1+"000000000000"+"00:00:00"+"00:00:00"
                                                       +status2+finsframework_id+fequipment_no+welder+electricity2+voltage2+wirefeedrate2+weldingrate2+weldheatinput2+hatwirecurrent2+vibrafrequency2+timesql2+"000000000000"+"00:00:00"+"00:00:00"
                                                       +status3+finsframework_id+fequipment_no+welder+electricity3+voltage3+wirefeedrate3+weldingrate3+weldheatinput3+hatwirecurrent3+vibrafrequency3+timesql3+"000000000000"+"00:00:00"+"00:00:00";
                                           }
                                           break;*/


                                                if (finsframework_id == null || finsframework_id == "") {
                                                    finsframework_id = "nu";
                                                    strsend += status1 + finsframework_id + fequipment_no + welder + electricity1 + voltage1 + wirefeedrate1 + weldingrate1 + weldheatinput1 + hatwirecurrent1 + vibrafrequency1 + timesql1 + limit + "00:00:00" + "00:00:00" + code
                                                            + status2 + finsframework_id + fequipment_no + welder + electricity2 + voltage2 + wirefeedrate2 + weldingrate2 + weldheatinput2 + hatwirecurrent2 + vibrafrequency2 + timesql2 + limit + "00:00:00" + "00:00:00" + code
                                                            + status3 + finsframework_id + fequipment_no + welder + electricity3 + voltage3 + wirefeedrate3 + weldingrate3 + weldheatinput3 + hatwirecurrent3 + vibrafrequency3 + timesql3 + limit + "00:00:00" + "00:00:00" + code;
                                                } else {
                                                    strsend += status1 + finsframework_id + fequipment_no + welder + electricity1 + voltage1 + wirefeedrate1 + weldingrate1 + weldheatinput1 + hatwirecurrent1 + vibrafrequency1 + timesql1 + limit + "00:00:00" + "00:00:00" + code
                                                            + status2 + finsframework_id + fequipment_no + welder + electricity2 + voltage2 + wirefeedrate2 + weldingrate2 + weldheatinput2 + hatwirecurrent2 + vibrafrequency2 + timesql2 + limit + "00:00:00" + "00:00:00" + code
                                                            + status3 + finsframework_id + fequipment_no + welder + electricity3 + voltage3 + wirefeedrate3 + weldingrate3 + weldheatinput3 + hatwirecurrent3 + vibrafrequency3 + timesql3 + limit + "00:00:00" + "00:00:00" + code;
                                                }
                                                break;

                                            }
                                        } catch (Exception e) {
                                            //e.getStackTrace();
                                        }
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            datawritetype = true;

                            synchronized (websocketlist) {

                                ArrayList<String> listarraybuf = new ArrayList<String>();
                                boolean ifdo = false;

                                Iterator<Entry<String, SocketChannel>> webiter = websocketlist.entrySet().iterator();
                                while (webiter.hasNext()) {
                                    try {
                                        Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
                                        websocketfail = entry.getKey();
                                        SocketChannel websocketcon = entry.getValue();
                                        websocketcon.writeAndFlush(new TextWebSocketFrame(strsend)).sync();
                                        ////System.out.println(strsend);
                                    } catch (Exception e) {

                                        listarraybuf.add(websocketfail);
                                        ifdo = true;

                                    }
                                }

                                if (ifdo) {
                                    for (int i = 0; i < listarraybuf.size(); i++) {
                                        websocketlist.remove(listarraybuf.get(i));
                                    }
                                }

                            }

                            strsend = "";


                        } else {
                            //System.out.println("校验位错误");
                            str = "";
                        }

                    } else {
                        //System.out.println("首末位错误");
                        str = "";
                    }

                } else if (str.length() == 110) {
                    String check1 = str.substring(0, 2);
                    String check11 = str.substring(108, 110);
                    if (check1.equals("FA") && check11.equals("F5")) {

                        //鏍￠獙闀垮害
                        int check2 = str.length();
                        if (check2 == 110) {

                            //鏍￠獙浣嶆牎楠?
                            String check3 = str.substring(2, 104);
                            String check5 = "";
                            int check4 = 0;
                            for (int i11 = 0; i11 < check3.length() / 2; i11++) {
                                String tstr1 = check3.substring(i11 * 2, i11 * 2 + 2);
                                check4 += Integer.valueOf(tstr1, 16);
                            }
                            if ((Integer.toHexString(check4)).toUpperCase().length() == 2) {
                                check5 = ((Integer.toHexString(check4)).toUpperCase());
                            } else {
                                check5 = ((Integer.toHexString(check4)).toUpperCase()).substring(1, 3);
                            }
                            String check6 = str.substring(104, 106);
                            if (check5.equals(check6)) {

                                ////System.out.println("2");

                                strdata = str;
                                //String weldname = strdata.substring(10,14);
                                int weldname1 = Integer.valueOf(strdata.substring(10, 14).toString(), 16);
                                String weldname = String.valueOf(weldname1);
                                if (weldname.length() != 4) {
                                    int lenth = 4 - weldname.length();
                                    for (int i = 0; i < lenth; i++) {
                                        weldname = "0" + weldname;
                                    }
                                }

                                int welder1 = Integer.valueOf(strdata.substring(14, 18).toString(), 16);
                                String welder = String.valueOf(welder1);
                                if (welder.length() != 4) {
                                    int lenth = 4 - welder.length();
                                    for (int i = 0; i < lenth; i++) {
                                        welder = "0" + welder;
                                    }
                                }

                                //String code = strdata.substring(18,26);
                                long code1 = Integer.valueOf(strdata.substring(18, 26).toString(), 16);
                                String code = String.valueOf(code1);
                                if (code.length() != 8) {
                                    int lenth = 8 - code.length();
                                    for (int i = 0; i < lenth; i++) {
                                        code = "0" + code;
                                    }
                                }

                                int electricity11 = Integer.valueOf(strdata.substring(26, 30).toString(), 16);
                                String electricity1 = String.valueOf(electricity11);
                                if (electricity1.length() != 4) {
                                    int lenth = 4 - electricity1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        electricity1 = "0" + electricity1;
                                    }
                                }

                                int voltage11 = Integer.valueOf(strdata.substring(30, 34).toString(), 16);
                                String voltage1 = String.valueOf(voltage11);
                                if (voltage1.length() != 4) {
                                    int lenth = 4 - voltage1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        voltage1 = "0" + voltage1;
                                    }
                                }

                                String status1 = Integer.valueOf(strdata.substring(38, 40), 16).toString();
                                if (status1.length() != 2) {
                                    int lenth = 2 - status1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        status1 = "0" + status1;
                                    }
                                }

                                long year1 = Integer.valueOf(str.substring(40, 42).toString(), 16);
                                String yearstr1 = String.valueOf(year1);
                                long month1 = Integer.valueOf(str.substring(42, 44).toString(), 16);
                                String monthstr1 = String.valueOf(month1);
                                if (monthstr1.length() != 2) {
                                    int lenth = 2 - monthstr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        monthstr1 = "0" + monthstr1;
                                    }
                                }
                                long day1 = Integer.valueOf(str.substring(44, 46).toString(), 16);
                                String daystr1 = String.valueOf(day1);
                                if (daystr1.length() != 2) {
                                    int lenth = 2 - daystr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        daystr1 = "0" + daystr1;
                                    }
                                }
                                long hour1 = Integer.valueOf(str.substring(46, 48).toString(), 16);
                                String hourstr1 = String.valueOf(hour1);
                                if (hourstr1.length() != 2) {
                                    int lenth = 2 - hourstr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        hourstr1 = "0" + hourstr1;
                                    }
                                }
                                long minute1 = Integer.valueOf(str.substring(48, 50).toString(), 16);
                                String minutestr1 = String.valueOf(minute1);
                                if (minutestr1.length() != 2) {
                                    int lenth = 2 - minutestr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        minutestr1 = "0" + minutestr1;
                                    }
                                }
                                long second1 = Integer.valueOf(str.substring(50, 52).toString(), 16);
                                String secondstr1 = String.valueOf(second1);
                                if (secondstr1.length() != 2) {
                                    int lenth = 2 - secondstr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        secondstr1 = "0" + secondstr1;
                                    }
                                }

                                String timestr1 = yearstr1 + "-" + monthstr1 + "-" + daystr1 + " " + hourstr1 + ":" + minutestr1 + ":" + secondstr1;
                                SimpleDateFormat timeshow1 = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                                try {

                                    Date time1 = DateTools.parse("yy-MM-dd HH:mm:ss", timestr1);
                                    //java.util.Date time4 = timeshow3.parse(timestr3);
                                    timesql1 = new Timestamp(time1.getTime());

                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    //e.printStackTrace();
                                }


                                int electricity22 = Integer.valueOf(strdata.substring(52, 56).toString(), 16);
                                String electricity2 = String.valueOf(electricity22);
                                if (electricity2.length() != 4) {
                                    int lenth = 4 - electricity2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        electricity2 = "0" + electricity2;
                                    }
                                }

                                int voltage22 = Integer.valueOf(strdata.substring(56, 60).toString(), 16);
                                String voltage2 = String.valueOf(voltage22);
                                if (voltage2.length() != 4) {
                                    int lenth = 4 - voltage2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        voltage2 = "0" + voltage2;
                                    }
                                }

                                String status2 = Integer.valueOf(strdata.substring(64, 66), 16).toString();
                                if (status2.length() != 2) {
                                    int lenth = 2 - status2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        status2 = "0" + status2;
                                    }
                                }

                                long year2 = Integer.valueOf(str.substring(66, 68).toString(), 16);
                                String yearstr2 = String.valueOf(year2);
                                long month2 = Integer.valueOf(str.substring(68, 70).toString(), 16);
                                String monthstr2 = String.valueOf(month2);
                                if (monthstr2.length() != 2) {
                                    int lenth = 2 - monthstr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        monthstr2 = "0" + monthstr2;
                                    }
                                }
                                long day2 = Integer.valueOf(str.substring(70, 72).toString(), 16);
                                String daystr2 = String.valueOf(day2);
                                if (daystr2.length() != 2) {
                                    int lenth = 2 - daystr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        daystr2 = "0" + daystr2;
                                    }
                                }
                                long hour2 = Integer.valueOf(str.substring(72, 74).toString(), 16);
                                String hourstr2 = String.valueOf(hour2);
                                if (hourstr2.length() != 2) {
                                    int lenth = 2 - hourstr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        hourstr2 = "0" + hourstr2;
                                    }
                                }
                                long minute2 = Integer.valueOf(str.substring(74, 76).toString(), 16);
                                String minutestr2 = String.valueOf(minute2);
                                if (minutestr2.length() != 2) {
                                    int lenth = 2 - minutestr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        minutestr2 = "0" + minutestr2;
                                    }
                                }
                                long second2 = Integer.valueOf(str.substring(76, 78).toString(), 16);
                                String secondstr2 = String.valueOf(second2);
                                if (secondstr2.length() != 2) {
                                    int lenth = 2 - secondstr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        secondstr2 = "0" + secondstr2;
                                    }
                                }

                                String timestr2 = yearstr2 + "-" + monthstr2 + "-" + daystr2 + " " + hourstr2 + ":" + minutestr2 + ":" + secondstr2;
                                SimpleDateFormat timeshow2 = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                                try {

                                    Date time2 = DateTools.parse("yy-MM-dd HH:mm:ss", timestr2);
                                    //java.util.Date time4 = timeshow3.parse(timestr3);
                                    timesql2 = new Timestamp(time2.getTime());

                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    //e.printStackTrace();
                                }


                                int electricity33 = Integer.valueOf(strdata.substring(52, 56).toString(), 16);
                                String electricity3 = String.valueOf(electricity33);
                                if (electricity3.length() != 4) {
                                    int lenth = 4 - electricity3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        electricity3 = "0" + electricity3;
                                    }
                                }

                                int voltage33 = Integer.valueOf(strdata.substring(56, 60).toString(), 16);
                                String voltage3 = String.valueOf(voltage33);
                                if (voltage3.length() != 4) {
                                    int lenth = 4 - voltage3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        voltage3 = "0" + voltage3;
                                    }
                                }
                                String status3 = Integer.valueOf(strdata.substring(90, 92), 16).toString();
                                if (status3.length() != 2) {
                                    int lenth = 2 - status3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        status3 = "0" + status3;
                                    }
                                }

                                long year3 = Integer.valueOf(str.substring(92, 94).toString(), 16);
                                String yearstr3 = String.valueOf(year3);
                                long month3 = Integer.valueOf(str.substring(94, 96).toString(), 16);
                                String monthstr3 = String.valueOf(month3);
                                if (monthstr3.length() != 2) {
                                    int lenth = 2 - monthstr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        monthstr3 = "0" + monthstr3;
                                    }
                                }
                                long day3 = Integer.valueOf(str.substring(96, 98).toString(), 16);
                                String daystr3 = String.valueOf(day3);
                                if (daystr3.length() != 2) {
                                    int lenth = 2 - daystr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        daystr3 = "0" + daystr3;
                                    }
                                }
                                long hour3 = Integer.valueOf(str.substring(98, 100).toString(), 16);
                                String hourstr3 = String.valueOf(hour3);
                                if (hourstr3.length() != 2) {
                                    int lenth = 2 - hourstr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        hourstr3 = "0" + hourstr3;
                                    }
                                }
                                long minute3 = Integer.valueOf(str.substring(100, 102).toString(), 16);
                                String minutestr3 = String.valueOf(minute3);
                                if (minutestr3.length() != 2) {
                                    int lenth = 2 - minutestr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        minutestr3 = "0" + minutestr3;
                                    }
                                }
                                long second3 = Integer.valueOf(str.substring(102, 104).toString(), 16);
                                String secondstr3 = String.valueOf(second3);
                                if (secondstr3.length() != 2) {
                                    int lenth = 2 - secondstr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        secondstr3 = "0" + secondstr3;
                                    }
                                }

                                String timestr3 = yearstr3 + "-" + monthstr3 + "-" + daystr3 + " " + hourstr3 + ":" + minutestr3 + ":" + secondstr3;
                                SimpleDateFormat timeshow3 = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                                try {
                                    Date time3 = DateTools.parse("yy-MM-dd HH:mm:ss", timestr3);
                                    //java.util.Date time4 = timeshow3.parse(timestr3);
                                    timesql3 = new Timestamp(time3.getTime());
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    //e.printStackTrace();
                                }

                                ////System.out.println("3");

                                try {

                                    for (int i = 0; i < listarray3.size(); i += 5) {
                                        String weldjunction = listarray3.get(i);
                                        if (weldjunction.equals(code)) {
                                            String maxe = listarray3.get(i + 1);
                                            String mixe = listarray3.get(i + 2);
                                            String maxv = listarray3.get(i + 3);
                                            String mixv = listarray3.get(i + 4);
                                            limit = maxe + mixe + maxv + mixv;
                                        }
                                    }

                                    ////System.out.println("4");

	                       	 /*String worktime = "";
		                   	 String totaltime = "";
		                   	 String worktime1 = "";
		                   	 String totaltime1 = "";
		                   	 String workhour1,workminute1,worksecond1;
		                   	 String workhour2,workminute2,worksecond2;*/
                                    for (int i = 0; i < listarray2.size(); i += 4) {
                                        //淇敼鍙戦€佺剨鏈虹紪鍙蜂负鐒婃満id
                                        //String fequipment_no = listarray2.get(i+1);
                                        String fequipment_no = listarray2.get(i);
                                        String fgather_no = listarray2.get(i + 2);
                                        String finsframework_id = listarray2.get(i + 3);

                                        //System.out.print(fequipment_no+" ");

                                        if (weldname.equals(fgather_no)) {

                                            try {
                                                if (fequipment_no.length() != 4) {
                                                    int lenth = 4 - fequipment_no.length();
                                                    for (int i1 = 0; i1 < lenth; i1++) {
                                                        fequipment_no = "0" + fequipment_no;
                                                    }
                                                }

                                                //江南
                                                if (weldname.equals(fgather_no)) {
                                                    if (finsframework_id == null || finsframework_id == "") {
                                                        finsframework_id = "nu";
                                                        strsend += status1 + finsframework_id + fequipment_no + welder + electricity1 + voltage1 + timesql1 + "300050045005" + "00:00:00" + "00:00:00"
                                                                + status2 + finsframework_id + fequipment_no + welder + electricity2 + voltage2 + timesql2 + "300050045005" + "00:00:00" + "00:00:00"
                                                                + status3 + finsframework_id + fequipment_no + welder + electricity3 + voltage3 + timesql3 + "300050045005" + "00:00:00" + "00:00:00";
                                                    } else {
                                                        strsend += status1 + finsframework_id + fequipment_no + welder + electricity1 + voltage1 + timesql1 + "300050045005" + "00:00:00" + "00:00:00"
                                                                + status2 + finsframework_id + fequipment_no + welder + electricity2 + voltage2 + timesql2 + "300050045005" + "00:00:00" + "00:00:00"
                                                                + status3 + finsframework_id + fequipment_no + welder + electricity3 + voltage3 + timesql3 + "300050045005" + "00:00:00" + "00:00:00";
                                                    }
                                                    break;
                                                }

                                            } catch (Exception e) {
                                                //e.getStackTrace();
                                            }

                                        }
                                    }
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    //System.out.println("数据库错误");
                                    //e.printStackTrace();
                                }

                                datawritetype = true;

                                synchronized (websocketlist) {

                                    ArrayList<String> listarraybuf = new ArrayList<String>();
                                    boolean ifdo = false;

                                    Iterator<Entry<String, SocketChannel>> webiter = websocketlist.entrySet().iterator();
                                    while (webiter.hasNext()) {
                                        try {
                                            Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
                                            websocketfail = entry.getKey();
                                            SocketChannel websocketcon = entry.getValue();
                                            websocketcon.writeAndFlush(new TextWebSocketFrame(strsend)).sync();

                                        } catch (Exception e) {

                                            listarraybuf.add(websocketfail);
                                            ifdo = true;

                                        }
                                    }

                                    if (ifdo) {
                                        for (int i = 0; i < listarraybuf.size(); i++) {
                                            websocketlist.remove(listarraybuf.get(i));
                                        }
                                    }

                                }

                                strsend = "";
                            } else {
                                //System.out.println("校验位错误");
                                str = "";
                            }

                        } else {
                            //System.out.println("长度错误");
                            str = "";
                        }

                    } else {
                        //System.out.println("首末位错误");
                        str = "";
                    }

                } else {

                    str = "";

                }
            }
        } catch (Exception e) {

            if (datawritetype = true) {

                //e.printStackTrace();

            }
        }
    }

    public Websocket(String str, java.sql.Statement stmt, HashMap<String, Socket> websocket, ArrayList<String> listarray2, ArrayList<String> listarray3, HashMap<String, SocketChannel> websocketlist, ArrayList<String> dbdata) {
        // TODO Auto-generated constructor stub

        this.strdata = str;
        this.websocket = websocket;
        this.listarray2 = listarray2;
        this.listarray3 = listarray3;
        this.websocketlist = websocketlist;
        this.dbdata = dbdata;

        try {

            if (websocketlist == null || websocketlist.isEmpty()) {

            } else {
                if (str.length() == 110) {

                    //校验第一位是否为FA末位是否为F5
                    String check1 = str.substring(0, 2);
                    String check11 = str.substring(108, 110);
                    if (check1.equals("FA") && check11.equals("F5")) {

                        //校验长度
                        int check2 = str.length();
                        if (check2 == 110) {

                            //校验位校验
                            String check3 = str.substring(2, 104);
                            String check5 = "";
                            int check4 = 0;
                            for (int i11 = 0; i11 < check3.length() / 2; i11++) {
                                String tstr1 = check3.substring(i11 * 2, i11 * 2 + 2);
                                check4 += Integer.valueOf(tstr1, 16);
                            }
                            if ((Integer.toHexString(check4)).toUpperCase().length() == 2) {
                                check5 = ((Integer.toHexString(check4)).toUpperCase());
                            } else {
                                check5 = ((Integer.toHexString(check4)).toUpperCase()).substring(1, 3);
                            }
                            String check6 = str.substring(104, 106);
                            if (check5.equals(check6)) {

                                strdata = str;
                                //String weldname = strdata.substring(10,14);
                                int weldname1 = Integer.valueOf(strdata.substring(10, 14).toString(), 16);
                                String weldname = String.valueOf(weldname1);
                                if (weldname.length() != 4) {
                                    int lenth = 4 - weldname.length();
                                    for (int i = 0; i < lenth; i++) {
                                        weldname = "0" + weldname;
                                    }
                                }

                                int welder1 = Integer.valueOf(strdata.substring(14, 18).toString(), 16);
                                String welder = String.valueOf(welder1);
                                if (welder.length() != 4) {
                                    int lenth = 4 - welder.length();
                                    for (int i = 0; i < lenth; i++) {
                                        welder = "0" + welder;
                                    }
                                }

                                //String code = strdata.substring(18,26);
                                long code1 = Integer.valueOf(strdata.substring(18, 26).toString(), 16);
                                String code = String.valueOf(code1);
                                if (code.length() != 8) {
                                    int lenth = 8 - code.length();
                                    for (int i = 0; i < lenth; i++) {
                                        code = "0" + code;
                                    }
                                }

                                int electricity11 = Integer.valueOf(strdata.substring(26, 30).toString(), 16);
                                String electricity1 = String.valueOf(electricity11);
                                if (electricity1.length() != 4) {
                                    int lenth = 4 - electricity1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        electricity1 = "0" + electricity1;
                                    }
                                }

                                int voltage11 = Integer.valueOf(strdata.substring(30, 34).toString(), 16);
                                String voltage1 = String.valueOf(voltage11);
                                if (voltage1.length() != 4) {
                                    int lenth = 4 - voltage1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        voltage1 = "0" + voltage1;
                                    }
                                }

                                String status1 = strdata.substring(38, 40);

                                long year1 = Integer.valueOf(str.substring(40, 42).toString(), 16);
                                String yearstr1 = String.valueOf(year1);
                                long month1 = Integer.valueOf(str.substring(42, 44).toString(), 16);
                                String monthstr1 = String.valueOf(month1);
                                if (monthstr1.length() != 2) {
                                    int lenth = 2 - monthstr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        monthstr1 = "0" + monthstr1;
                                    }
                                }
                                long day1 = Integer.valueOf(str.substring(44, 46).toString(), 16);
                                String daystr1 = String.valueOf(day1);
                                if (daystr1.length() != 2) {
                                    int lenth = 2 - daystr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        daystr1 = "0" + daystr1;
                                    }
                                }
                                long hour1 = Integer.valueOf(str.substring(46, 48).toString(), 16);
                                String hourstr1 = String.valueOf(hour1);
                                if (hourstr1.length() != 2) {
                                    int lenth = 2 - hourstr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        hourstr1 = "0" + hourstr1;
                                    }
                                }
                                long minute1 = Integer.valueOf(str.substring(48, 50).toString(), 16);
                                String minutestr1 = String.valueOf(minute1);
                                if (minutestr1.length() != 2) {
                                    int lenth = 2 - minutestr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        minutestr1 = "0" + minutestr1;
                                    }
                                }
                                long second1 = Integer.valueOf(str.substring(50, 52).toString(), 16);
                                String secondstr1 = String.valueOf(second1);
                                if (secondstr1.length() != 2) {
                                    int lenth = 2 - secondstr1.length();
                                    for (int i = 0; i < lenth; i++) {
                                        secondstr1 = "0" + secondstr1;
                                    }
                                }

                                String timestr1 = yearstr1 + "-" + monthstr1 + "-" + daystr1 + " " + hourstr1 + ":" + minutestr1 + ":" + secondstr1;
                                SimpleDateFormat timeshow1 = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                                try {

                                    Date time1 = DateTools.parse("yy-MM-dd HH:mm:ss", timestr1);
                                    //java.util.Date time4 = timeshow3.parse(timestr3);
                                    timesql1 = new Timestamp(time1.getTime());

                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    //e.printStackTrace();
                                }


                                int electricity22 = Integer.valueOf(strdata.substring(52, 56).toString(), 16);
                                String electricity2 = String.valueOf(electricity22);
                                if (electricity2.length() != 4) {
                                    int lenth = 4 - electricity2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        electricity2 = "0" + electricity2;
                                    }
                                }

                                int voltage22 = Integer.valueOf(strdata.substring(56, 60).toString(), 16);
                                String voltage2 = String.valueOf(voltage22);
                                if (voltage2.length() != 4) {
                                    int lenth = 4 - voltage2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        voltage2 = "0" + voltage2;
                                    }
                                }
                                String status2 = strdata.substring(64, 66);

                                long year2 = Integer.valueOf(str.substring(66, 68).toString(), 16);
                                String yearstr2 = String.valueOf(year2);
                                long month2 = Integer.valueOf(str.substring(68, 70).toString(), 16);
                                String monthstr2 = String.valueOf(month2);
                                if (monthstr2.length() != 2) {
                                    int lenth = 2 - monthstr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        monthstr2 = "0" + monthstr2;
                                    }
                                }
                                long day2 = Integer.valueOf(str.substring(70, 72).toString(), 16);
                                String daystr2 = String.valueOf(day2);
                                if (daystr2.length() != 2) {
                                    int lenth = 2 - daystr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        daystr2 = "0" + daystr2;
                                    }
                                }
                                long hour2 = Integer.valueOf(str.substring(72, 74).toString(), 16);
                                String hourstr2 = String.valueOf(hour2);
                                if (hourstr2.length() != 2) {
                                    int lenth = 2 - hourstr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        hourstr2 = "0" + hourstr2;
                                    }
                                }
                                long minute2 = Integer.valueOf(str.substring(74, 76).toString(), 16);
                                String minutestr2 = String.valueOf(minute2);
                                if (minutestr2.length() != 2) {
                                    int lenth = 2 - minutestr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        minutestr2 = "0" + minutestr2;
                                    }
                                }
                                long second2 = Integer.valueOf(str.substring(76, 78).toString(), 16);
                                String secondstr2 = String.valueOf(second2);
                                if (secondstr2.length() != 2) {
                                    int lenth = 2 - secondstr2.length();
                                    for (int i = 0; i < lenth; i++) {
                                        secondstr2 = "0" + secondstr2;
                                    }
                                }

                                String timestr2 = yearstr2 + "-" + monthstr2 + "-" + daystr2 + " " + hourstr2 + ":" + minutestr2 + ":" + secondstr2;
                                SimpleDateFormat timeshow2 = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                                try {

                                    Date time2 = DateTools.parse("yy-MM-dd HH:mm:ss", timestr2);
                                    //java.util.Date time4 = timeshow3.parse(timestr3);
                                    timesql2 = new Timestamp(time2.getTime());

                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    //e.printStackTrace();
                                }


                                int electricity33 = Integer.valueOf(strdata.substring(52, 56).toString(), 16);
                                String electricity3 = String.valueOf(electricity33);
                                if (electricity3.length() != 4) {
                                    int lenth = 4 - electricity3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        electricity3 = "0" + electricity3;
                                    }
                                }

                                int voltage33 = Integer.valueOf(strdata.substring(56, 60).toString(), 16);
                                String voltage3 = String.valueOf(voltage33);
                                if (voltage3.length() != 4) {
                                    int lenth = 4 - voltage3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        voltage3 = "0" + voltage3;
                                    }
                                }
                                String status3 = strdata.substring(90, 92);

                                long year3 = Integer.valueOf(str.substring(92, 94).toString(), 16);
                                String yearstr3 = String.valueOf(year3);
                                long month3 = Integer.valueOf(str.substring(94, 96).toString(), 16);
                                String monthstr3 = String.valueOf(month3);
                                if (monthstr3.length() != 2) {
                                    int lenth = 2 - monthstr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        monthstr3 = "0" + monthstr3;
                                    }
                                }
                                long day3 = Integer.valueOf(str.substring(96, 98).toString(), 16);
                                String daystr3 = String.valueOf(day3);
                                if (daystr3.length() != 2) {
                                    int lenth = 2 - daystr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        daystr3 = "0" + daystr3;
                                    }
                                }
                                long hour3 = Integer.valueOf(str.substring(98, 100).toString(), 16);
                                String hourstr3 = String.valueOf(hour3);
                                if (hourstr3.length() != 2) {
                                    int lenth = 2 - hourstr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        hourstr3 = "0" + hourstr3;
                                    }
                                }
                                long minute3 = Integer.valueOf(str.substring(100, 102).toString(), 16);
                                String minutestr3 = String.valueOf(minute3);
                                if (minutestr3.length() != 2) {
                                    int lenth = 2 - minutestr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        minutestr3 = "0" + minutestr3;
                                    }
                                }
                                long second3 = Integer.valueOf(str.substring(102, 104).toString(), 16);
                                String secondstr3 = String.valueOf(second3);
                                if (secondstr3.length() != 2) {
                                    int lenth = 2 - secondstr3.length();
                                    for (int i = 0; i < lenth; i++) {
                                        secondstr3 = "0" + secondstr3;
                                    }
                                }

                                String timestr3 = yearstr3 + "-" + monthstr3 + "-" + daystr3 + " " + hourstr3 + ":" + minutestr3 + ":" + secondstr3;
                                SimpleDateFormat timeshow3 = new SimpleDateFormat("yy-MM-dd hh:mm:ss");
                                try {
                                    Date time3 = DateTools.parse("yy-MM-dd HH:mm:ss", timestr3);
                                    //java.util.Date time4 = timeshow3.parse(timestr3);
                                    timesql3 = new Timestamp(time3.getTime());
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    //e.printStackTrace();
                                }

                                try {

                                    for (int i = 0; i < listarray3.size(); i += 5) {
                                        String weldjunction = listarray3.get(i);
                                        if (weldjunction.equals(code)) {
                                            String maxe = listarray3.get(i + 1);
                                            String mixe = listarray3.get(i + 2);
                                            String maxv = listarray3.get(i + 3);
                                            String mixv = listarray3.get(i + 4);
                                            limit = maxe + mixe + maxv + mixv;
                                        }
                                    }

	                       	 /*String worktime = "";
		                   	 String totaltime = "";
		                   	 String worktime1 = "";
		                   	 String totaltime1 = "";
		                   	 String workhour1,workminute1,worksecond1;
		                   	 String workhour2,workminute2,worksecond2;*/
                                    for (int i = 0; i < listarray2.size(); i += 4) {
                                        String fequipment_no = listarray2.get(i);
                                        String fgather_no = listarray2.get(i + 2);
                                        String finsframework_id = listarray2.get(i + 3);

                                        if (weldname.equals(fgather_no)) {

		                   			 /*for(int j=0;j<dbdata.size();j+=3){
		                       			 if(dbdata.get(j).equals(fequipment_no)){
		                       				 if(status1.equals("00")){
		                       					 worktime=Integer.toString(Integer.valueOf(dbdata.get(j+1)));
		                       					 totaltime=Integer.toString(Integer.valueOf(dbdata.get(j+2))+3);
		                       					 dbdata.set(j+2, totaltime);
		                       				 }else{
		                       					 worktime=Integer.toString(Integer.valueOf(dbdata.get(j+1))+3);
		                       					 totaltime=Integer.toString(Integer.valueOf(dbdata.get(j+2))+3);

		                       					 dbdata.set(j+1, worktime);
		                       					 dbdata.set(j+2, totaltime);
		                       				 }
		                       				 break;
		                       			 }
		                       		 }

		                   			 long hour = (long)Integer.valueOf(worktime)/3600;
	                 		        if(hour<10){
	                 		        	workhour1 = "0" + String.valueOf(hour) + ":";
	                 		        }else{
	                 		        	workhour1 = String.valueOf(hour) + ":";
	                 		        }
	                 		        long last = (long)Integer.valueOf(worktime)%3600;
	                 		        long minute = last/60;
	                 		        if(minute<10){
	                 		        	workminute1 = "0" + String.valueOf(minute) + ":";
	                 		        }else{
	                 		        	workminute1 = String.valueOf(minute) + ":";
	                 		        }
	                 		        long second = last%60;
	                 		        if(second<10){
	                 		        	worksecond1 = "0" + String.valueOf(second);
	                 		        }else{
	                 		        	worksecond1 = String.valueOf(second);
	                 		        }
	                 		        worktime1 = workhour1 + workminute1 + worksecond1;

		               		        long hour4 = (long)Integer.valueOf(totaltime)/3600;
		               		        if(hour4<10){
		               		        	workhour2 = "0" + String.valueOf(hour4) + ":";
		               		        }else{
		               		        	workhour2 = String.valueOf(hour4) + ":";
		               		        }
		               		        long last4 = (long)Integer.valueOf(totaltime)%3600;
		               		        long minute4 = last4/60;
		               		        if(minute4<10){
		               		        	workminute2 = "0" + String.valueOf(minute4) + ":";
		               		        }else{
		               		        	workminute2 = String.valueOf(minute4) + ":";
		               		        }
		               		        long second4 = last4%60;
		               		        if(second4<10){
		               		        	worksecond2 = "0" + String.valueOf(second4);
		               		        }else{
		               		        	worksecond2 = String.valueOf(second4);
		               		        }
		               		         totaltime1 = workhour2 + workminute2 + worksecond2;*/

                                            if (fequipment_no.length() != 4) {
                                                int lenth = 4 - fequipment_no.length();
                                                for (int i1 = 0; i1 < lenth; i1++) {
                                                    fequipment_no = "0" + fequipment_no;
                                                }
                                            }

                                            if (weldname.equals(fgather_no)) {

                                                //江南
                                                if (finsframework_id == null || finsframework_id == "") {
                                                    finsframework_id = "nu";
                                                    strsend += status1 + finsframework_id + fequipment_no + welder + electricity1 + voltage1 + timesql1 + "000000000000" + "00:00:00" + "00:00:00"
                                                            + status2 + finsframework_id + fequipment_no + welder + electricity2 + voltage2 + timesql2 + "000000000000" + "00:00:00" + "00:00:00"
                                                            + status3 + finsframework_id + fequipment_no + welder + electricity3 + voltage3 + timesql3 + "000000000000" + "00:00:00" + "00:00:00";
                                                } else {
                                                    strsend += status1 + finsframework_id + fequipment_no + welder + electricity1 + voltage1 + timesql1 + "000000000000" + "00:00:00" + "00:00:00"
                                                            + status2 + finsframework_id + fequipment_no + welder + electricity2 + voltage2 + timesql2 + "000000000000" + "00:00:00" + "00:00:00"
                                                            + status3 + finsframework_id + fequipment_no + welder + electricity3 + voltage3 + timesql3 + "000000000000" + "00:00:00" + "00:00:00";
                                                }

	                       			/*if(finsframework_id==null || finsframework_id==""){
	                       				finsframework_id="nu";
	                       				strsend+=status1+finsframework_id+fequipment_no+welder+electricity1+voltage1+timesql1+limit+"00:00:00"+"00:00:00"
			   	                    			+status2+finsframework_id+fequipment_no+welder+electricity2+voltage2+timesql2+limit+"00:00:00"+"00:00:00"
			   	                    			+status3+finsframework_id+fequipment_no+welder+electricity3+voltage3+timesql3+limit+"00:00:00"+"00:00:00";
	                       			}else{
	                       				strsend+=status1+finsframework_id+fequipment_no+welder+electricity1+voltage1+timesql1+limit+"00:00:00"+"00:00:00"
			   	                    			+status2+finsframework_id+fequipment_no+welder+electricity2+voltage2+timesql2+limit+"00:00:00"+"00:00:00"
			   	                    			+status3+finsframework_id+fequipment_no+welder+electricity3+voltage3+timesql3+limit+"00:00:00"+"00:00:00";
	                       			}	*/
                                            } else {
                                                if (finsframework_id == null || finsframework_id == "") {
                                                    finsframework_id = "nu";
                                                    strsend += "09" + finsframework_id + fequipment_no + "0000" + "0000" + "0000" + "000000000000000000000" + "000000000000" + "00:00:00" + "00:00:00"
                                                            + "09" + finsframework_id + fequipment_no + "0000" + "0000" + "0000" + "000000000000000000000" + "000000000000" + "00:00:00" + "00:00:00"
                                                            + "09" + finsframework_id + fequipment_no + "0000" + "0000" + "0000" + "000000000000000000000" + "000000000000" + "00:00:00" + "00:00:00";
                                                } else {
                                                    strsend += "09" + finsframework_id + fequipment_no + "0000" + "0000" + "0000" + "000000000000000000000" + "000000000000" + "00:00:00" + "00:00:00"
                                                            + "09" + finsframework_id + fequipment_no + "0000" + "0000" + "0000" + "000000000000000000000" + "000000000000" + "00:00:00" + "00:00:00"
                                                            + "09" + finsframework_id + fequipment_no + "0000" + "0000" + "0000" + "000000000000000000000" + "000000000000" + "00:00:00" + "00:00:00";
                                                }
                                            }
                                        }
                                        ////System.out.println("2");
                                    }

                                    ////System.out.println(strsend);

                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    //System.out.println("数据库读取数据错误");
                                    //e.printStackTrace();
                                }

                                datawritetype = true;

                                Iterator<Entry<String, SocketChannel>> webiter = websocketlist.entrySet().iterator();
                                while (webiter.hasNext()) {
                                    try {
                                        Entry<String, SocketChannel> entry = (Entry<String, SocketChannel>) webiter.next();
                                        websocketfail = entry.getKey();
                                        SocketChannel websocketcon = entry.getValue();
                                        websocketcon.writeAndFlush(new TextWebSocketFrame(strsend)).sync();
                                    } catch (Exception e) {
                                        if (datawritetype = true) {
                                            websocketlist.remove(websocketfail);
                                            webiter = websocketlist.entrySet().iterator();
                                            datawritetype = false;
                                        }
                                    }
                                }

                                strsend = "";

                            } else {
                                //校验位错误
                                //System.out.println("数据接收校验位错误");
                                str = "";
                            }

                        } else {
                            //长度错误
                            //System.out.println("数据接收长度错误");
                            str = "";
                        }

                    } else {
                        //首位不是FE
                        //System.out.println("数据接收首末位错误");
                        str = "";
                    }


                } else {

                    str = "";

                }
            }
        } catch (Exception e) {

            if (datawritetype = true) {

                chweb = null;
                websocket.remove(websocketfail);
                datawritetype = false;

            }
        }

    }

    public Websocket() {
        // TODO Auto-generated constructor stub
    }
}
