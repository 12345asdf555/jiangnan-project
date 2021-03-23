package com.yang.serialport.ui;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.datacontract.schemas._2004._07.jn_weld_service.CompositeType;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.tempuri.WeldServiceStub;
import service.weld.jn.ServiceCall;
import service.weld.jn.ServiceCallResponse;

import javax.swing.*;
import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//接收焊机数据处理
@Sharable
public class NettyServerHandler extends ChannelHandlerAdapter {

    public String fitemid;
    public ArrayList<String> listarrayJN = new ArrayList<String>();  //任务、焊工、焊机、状态
    public JTextArea dataView = new JTextArea();
    public Date timetran;
    public long timetran1;
    public Date time11;
    public long timetran2;
    public Date time22;
    public long timetran3;
    public Date time33;
    public WeldServiceStub stu;
    public HashMap<String, String> hm;
    private boolean first1 = true;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //private static final ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    /**
     * 在线焊机的连接通道
     */
    private static ConcurrentHashMap<String, JSONArray> MAP = new ConcurrentHashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = null;
        byte[] req = null;
        String str = "";
        try {
            buf = (ByteBuf) msg;
            req = new byte[buf.readableBytes()];
            buf.readBytes(req);
            for (int i = 0; i < req.length; i++) {
                //判断为数字还是字母，若为字母+256取正数
                if (req[i] < 0) {
                    String r = Integer.toHexString(req[i] + 256);
                    String rr = r.toUpperCase();
                    //数字补为两位数
                    if (rr.length() == 1) {
                        rr = '0' + rr;
                    }
                    //strdata为总接收数据
                    str += rr;

                } else {
                    String r = Integer.toHexString(req[i]);
                    if (r.length() == 1)
                        r = '0' + r;
                    r = r.toUpperCase();
                    str += r;
                }
            }
            publishData(str, ctx);
        } catch (Exception e) {
            System.out.println("1");
            e.printStackTrace();
        } finally {
            buf.release();
            ctx.flush();
        }
    }

    private void publishData(String str, ChannelHandlerContext ctx) {
        if (str.length() >= 6) {
            if (str.substring(0, 2).equals("7E") && (str.substring(10, 12).equals("22")) && str.length() == 282) {
                //int gatherno = Integer.parseInt(str.substring(16, 20), 16);//采集编号
                //cachedThreadPool.execute(new MapGatherno(gatherno, ctx));
                str = trans(str); //融合有无任务模式
                str = str.substring(0, 280) + fitemid + "7D";
                try {
                    if (StaticClass.chcli.isWritable()) {
                        StaticClass.chcli.writeAndFlush(str).sync();
                        dataView.append(" " + str + "\r\n");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    dataView.setText("服务器未开启" + "\r\n");
                }
            } else if (str.substring(0, 2).equals("FA")) {
                str = str.substring(0, 106) + fitemid + "F5";
                try {
                    StaticClass.chcli.writeAndFlush(str).sync();
                    dataView.append("实时:" + str + "\r\n");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    dataView.setText("服务器未开启" + "\r\n");
                }
            } else {
                try {
                    if (StaticClass.chcli.isWritable()) {
                        StaticClass.chcli.writeAndFlush(str).sync();
                    }
                    dataView.append("上行:" + str + "\r\n");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    dataView.setText("服务器未开启" + "\r\n");
                }
                //int gatherno = Integer.parseInt(str.substring(16, 20), 16);//采集编号
                //cachedThreadPool.execute(new MapGatherno(gatherno, ctx));
            }
        }
    }

    public class MapGatherno implements java.lang.Runnable {

        private int gatherno;
        private ChannelHandlerContext ctx;

        public MapGatherno(int gatherno, ChannelHandlerContext ctx) {
            this.gatherno = gatherno;
            this.ctx = ctx;
        }

        @Override
        public void run() {
            InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
            String clientIp = insocket.getAddress().getHostAddress();
            int clientPort = insocket.getPort();
            String address = clientIp + ":" + clientPort;
            if (MAP.size() > 0) {
                //判断是否存在该采集编号，存在：判断开机时间
                if (MAP.containsKey(String.valueOf(gatherno))) {
                    JSONArray jsonArray = MAP.get(String.valueOf(gatherno));
                    for (Object o : jsonArray) {
                        JSONObject jsonObject = (JSONObject) o;
                        //开机时间不存在：新增
                        if (!jsonObject.containsKey("startTime")) {
                            jsonObject.put("startTime", sdf.format(System.currentTimeMillis()));
                        }
                        jsonObject.put("address", address);
                    }
                } else {
                    //采集编号不存在，则新增，并赋值开机时间
                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("startTime", sdf.format(System.currentTimeMillis()));
                    jsonObject.put("address", address);
                    jsonArray.add(jsonObject);
                    MAP.put(String.valueOf(gatherno), jsonArray);
                }
            } else {
                JSONArray jsonArray = new JSONArray();
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("startTime", sdf.format(System.currentTimeMillis()));
                jsonObject.put("address", address);
                jsonArray.add(jsonObject);
                MAP.put(String.valueOf(gatherno), jsonArray);
            }
        }
    }

    private String trans(String str) {

        if (str.length() == 282) {

            //校验第一位是否为FA末位是否为F5
            String check1 = str.substring(0, 2);
            String check11 = str.substring(280, 282);
            if (check1.equals("7E") && check11.equals("7D")) {

                //校验位校验
                String check3 = str.substring(0, 278);
                String check5 = "";
                int check4 = 0;
                for (int i11 = 0; i11 < check3.length() / 2; i11++) {
                    String tstr1 = check3.substring(i11 * 2, i11 * 2 + 2);
                    check4 += Integer.valueOf(tstr1, 16);
                }
                if ((Integer.toHexString(check4)).toUpperCase().length() == 3) {
                    check5 = ((Integer.toHexString(check4)).toUpperCase()).substring(1, 3);
                } else {
                    check5 = ((Integer.toHexString(check4)).toUpperCase()).substring(2, 4);
                }
                String check6 = str.substring(278, 280);
                if (check6.equals(check6)) {

                    StringBuilder sb = new StringBuilder(str);
                    str = sb.toString();

                    String weld = str.substring(14, 18);
                    String welder = str.substring(34, 38);
                    String junction1 = str.substring(70, 78);
                    String junction2 = str.substring(150, 158);
                    String junction3 = str.substring(230, 238);

                    //江南任务模式
                    if (Integer.parseInt(welder, 16) == 0 && Integer.parseInt(junction1, 16) == 0 && Integer.parseInt(junction2, 16) == 0 && Integer.parseInt(junction3, 16) == 0) {

                        //焊机编号对应id
                        int countweld = 0;
                        String weldid = "";
                        if (StaticClass.listweld.size() == 0) {
                            sb.replace(14, 18, "0000");
                            sb.replace(18, 22, "0000");
                        } else {
                            for (int a = 0; a < StaticClass.listweld.size(); a += 4) {
                                if (Integer.parseInt(StaticClass.listweld.get(a + 1)) == (Integer.parseInt(weld, 16))) {
                                    String gatherid = StaticClass.listweld.get(a);
                                    weldid = StaticClass.listweld.get(a + 2);

                                    if (gatherid.length() != 4) {
                                        int length = 4 - gatherid.length();
                                        for (int b = 0; b < length; b++) {
                                            gatherid = "0" + gatherid;
                                        }
                                    }

                                    if (weldid.length() != 4) {
                                        int length = 4 - weldid.length();
                                        for (int b = 0; b < length; b++) {
                                            weldid = "0" + weldid;
                                        }
                                    }

                                    sb.replace(14, 18, gatherid);
                                    sb.replace(18, 22, weldid);
                                    countweld = 0;

                                    break;
                                } else {
                                    countweld++;
                                    if (countweld == StaticClass.listweld.size() / 4) {
                                        sb.replace(14, 18, "0000");
                                        sb.replace(18, 22, "0000");
                                        countweld = 0;
                                    }
                                }
                            }
                        }

                        //江南任务下发,重置焊工id,焊口(任务id)
                        if (StaticClass.listarrayJN.size() == 0) {
                            sb.replace(34, 38, "0000");
                            sb.replace(70, 78, "00000000");
                            sb.replace(150, 158, "00000000");
                            sb.replace(230, 238, "00000000");
                        } else {
                            sb.replace(34, 38, "0000");
                            sb.replace(70, 78, "00000000");
                            sb.replace(150, 158, "00000000");
                            sb.replace(230, 238, "00000000");
                            String welder1 = "0000";
                            String code = "00000000";
                            for (int i = 0; i < StaticClass.listarrayJN.size(); i += 5) {
                                if (weldid.equals("")) {
                                    welder1 = "0000";
                                    code = "00000000";
                                } else {
                                    if (Integer.valueOf(weldid).toString().equals(StaticClass.listarrayJN.get(i + 2))) {
                                        welder1 = StaticClass.listarrayJN.get(i + 1);
                                        if (welder1 != "") {
                                            if (welder1.length() < 4) {
                                                int length = 4 - welder1.length();
                                                for (int j = 0; j < length; j++) {
                                                    welder1 = "0" + welder1;
                                                }
                                            }
                                        } else {
                                            welder1 = "0000";
                                        }

                                        code = StaticClass.listarrayJN.get(i);
                                        if (code != "") {
                                            if (code.length() != 8) {
                                                int length = 8 - code.length();
                                                for (int i1 = 0; i1 < length; i1++) {
                                                    code = "0" + code;
                                                }
                                            }
                                            code.toUpperCase();
                                        } else {
                                            code = "00000000";
                                        }
                                    }
                                }
                            }
                            sb.replace(34, 38, welder1);
                            sb.replace(70, 78, code);
                            sb.replace(150, 158, code);
                            sb.replace(230, 238, code);
                        }

                        str = sb.toString();

                    } else {

                        //无任务下发模式
                        int countweld = 0;
                        int countwelder = 0;
                        int countjunction = 0;

                        //焊机编号对应id
                        if (StaticClass.listweld.size() == 0) {
                            sb.replace(14, 18, "0000");
                            sb.replace(18, 22, "0000");
                        } else {
                            for (int a = 0; a < StaticClass.listweld.size(); a += 4) {
                                if (Integer.valueOf(StaticClass.listweld.get(a + 1)) == (Integer.parseInt(weld, 16))) {
                                    String gatherid = StaticClass.listweld.get(a);
                                    String weldid = StaticClass.listweld.get(a + 2);

                                    if (gatherid.length() != 4) {
                                        int length = 4 - gatherid.length();
                                        for (int b = 0; b < length; b++) {
                                            gatherid = "0" + gatherid;
                                        }
                                    }

                                    if (weldid.length() != 4) {
                                        int length = 4 - weldid.length();
                                        for (int b = 0; b < length; b++) {
                                            weldid = "0" + weldid;
                                        }
                                    }

                                    sb.replace(14, 18, gatherid);
                                    sb.replace(18, 22, weldid);
                                    countweld = 0;

                                } else {
                                    countweld++;
                                    if (countweld == StaticClass.listweld.size() / 4) {
                                        sb.replace(14, 18, "0000");
                                        sb.replace(18, 22, "0000");
                                        countweld = 0;
                                    }
                                }
                            }
                        }

                        //焊工编号对应id
                        if (StaticClass.listwelder.size() == 0) {
                            sb.replace(34, 38, "0000");
                        } else {
                            for (int a = 0; a < StaticClass.listwelder.size(); a += 2) {
                                if (Integer.valueOf(StaticClass.listwelder.get(a + 1)) == (Integer.parseInt(welder, 16))) {
                                    String welderid = StaticClass.listwelder.get(a);

                                    if (welderid.length() != 4) {
                                        int length = 4 - welderid.length();
                                        for (int b = 0; b < length; b++) {
                                            welderid = "0" + welderid;
                                        }
                                    }

                                    sb.replace(34, 38, welderid);
                                    countwelder = 0;

                                } else {
                                    countwelder++;
                                    if (countwelder == StaticClass.listwelder.size() / 2) {
                                        sb.replace(34, 38, "0000");
                                        countwelder = 0;
                                    }
                                }
                            }
                        }

                        //焊口编号对应id(有三组数据的焊口)
                        if (StaticClass.listjunction.size() == 0) {
                            sb.replace(70, 78, "00000000");
                            sb.replace(150, 158, "00000000");
                            sb.replace(230, 238, "00000000");
                        } else {
                            for (int a = 0; a < StaticClass.listjunction.size(); a += 2) {
                                if (Integer.valueOf(StaticClass.listjunction.get(a + 1)) == (Integer.parseInt(junction1, 16))) {
                                    String junctionid = StaticClass.listjunction.get(a);

                                    if (junctionid.length() != 8) {
                                        int length = 8 - junctionid.length();
                                        for (int b = 0; b < length; b++) {
                                            junctionid = "0" + junctionid;
                                        }
                                    }

                                    sb.replace(70, 78, junctionid);
                                    countjunction = 0;

                                } else {
                                    countjunction++;
                                    if (countjunction == StaticClass.listjunction.size() / 2) {
                                        sb.replace(70, 78, "00000000");
                                        countjunction = 0;
                                    }
                                }
                            }

                            for (int a = 0; a < StaticClass.listjunction.size(); a += 2) {
                                if (Integer.valueOf(StaticClass.listjunction.get(a + 1)) == (Integer.parseInt(junction2, 16))) {
                                    String junctionid = StaticClass.listjunction.get(a);

                                    if (junctionid.length() != 8) {
                                        int length = 8 - junctionid.length();
                                        for (int b = 0; b < length; b++) {
                                            junctionid = "0" + junctionid;
                                        }
                                    }

                                    sb.replace(150, 158, junctionid);
                                    countjunction = 0;

                                } else {
                                    countjunction++;
                                    if (countjunction == StaticClass.listjunction.size() / 2) {
                                        sb.replace(150, 158, "00000000");
                                        countjunction = 0;
                                    }
                                }
                            }

                            for (int a = 0; a < StaticClass.listjunction.size(); a += 2) {
                                if (Integer.valueOf(StaticClass.listjunction.get(a + 1)) == (Integer.parseInt(junction3, 16))) {
                                    String junctionid = StaticClass.listjunction.get(a);

                                    if (junctionid.length() != 8) {
                                        int length = 8 - junctionid.length();
                                        for (int b = 0; b < length; b++) {
                                            junctionid = "0" + junctionid;
                                        }
                                    }

                                    sb.replace(230, 238, junctionid);
                                    countjunction = 0;

                                } else {
                                    countjunction++;
                                    if (countjunction == StaticClass.listjunction.size() / 2) {
                                        sb.replace(230, 238, "00000000");
                                        countjunction = 0;
                                    }
                                }
                            }
                        }
                    }

                    str = sb.toString();

                }
            }
        }
        return str;
    }

    public Runnable tranpanrun = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            int count1 = 0;
            Date dt1 = null;
            Date dt2 = null;
            Date dt3 = null;

            dt1 = new Date();
            List<Element> nodes = null;
            try {
                ServiceCall sc = new ServiceCall();

                CompositeType tt = new CompositeType();
                tt.setWeldDataTable("");
                tt.setCmdCode(603220101);

                sc.setCmd(tt);

                ServiceCallResponse a = stu.serviceCall(sc);
                CompositeType rs = a.getServiceCallResult();
                String xml = rs.getWeldDataTable();

                //dt3 = new Date();
                stu._getServiceClient().cleanupTransport();

                Document doc = DocumentHelper.parseText(xml);

                Element rootElt = doc.getRootElement(); // 获取根节点

                nodes = rootElt.elements("dt");
            } catch (Exception e) {
                dataView.setText("wcf服务器未开启" + "\r\n");
                e.printStackTrace();
            }

            if (nodes != null) {
                String str1 = "";
                for (Iterator<Element> it = nodes.iterator(); it.hasNext(); ) {
                    count1++;
                    Element elm = it.next();

                    Element elmbuf1 = elm.element("state");

                    if (!elmbuf1.getStringValue().equals("关闭")) {
                        //System.out.println(elmbuf1.getStringValue());
						/*                    for(Iterator it1=elm.elementIterator();it1.hasNext();){
			                    Element element = (Element) it1.next();
			                    json.put(element.getName(), element.getStringValue());
			                    System.out.println("点：" + element.getName() + " " + element.getStringValue()); // 拿到根节点的名称
			                };*/
                        str1 = "7E730101012280";
                        String nom = Integer.toHexString(Integer.valueOf(elm.element("nom").getStringValue())); //设备编号
                        if (nom.length() < 4) {
                            int len = 4 - nom.length();
                            for (int i = 0; i < len; i++) {
                                nom = "0" + nom;
                            }
                        }

                        int countweld = 0;          //设备编号对应任务信息得到焊机编号
                        String weldid = "";
                        if (StaticClass.listweld.size() == 0) {
                            str1 = str1 + "00000000";
                        } else {
                            for (int a1 = 0; a1 < StaticClass.listweld.size(); a1 += 4) {
                                if (Integer.valueOf(StaticClass.listweld.get(a1 + 1)) == (Integer.parseInt(nom, 16))) {
                                    String gatherid = StaticClass.listweld.get(a1);
                                    weldid = StaticClass.listweld.get(a1 + 2);

                                    if (gatherid.length() != 4) {
                                        int length = 4 - gatherid.length();
                                        for (int b = 0; b < length; b++) {
                                            gatherid = "0" + gatherid;
                                        }
                                    }
                                    if (weldid.length() != 4) {
                                        int length = 4 - weldid.length();
                                        for (int b = 0; b < length; b++) {
                                            weldid = "0" + weldid;
                                        }
                                    }

                                    str1 = str1 + gatherid + weldid;
                                    countweld = 0;

                                    break;
                                } else {
                                    countweld++;
                                    if (countweld == StaticClass.listweld.size() / 4) {
                                        str1 = str1 + "00000000";
                                        countweld = 0;
                                    }
                                }
                            }
                        }

                        listarrayJN = StaticClass.listarrayJN;
                        if (StaticClass.listarrayJN.size() == 0) {             //设备编号对应任务信息得到焊工信息
                            str1 = str1 + "0000000000000000";
                        } else {
                            String welder1 = "0000000000000000";
                            int count = 0;
                            for (int i = 0; i < StaticClass.listarrayJN.size(); i += 5) {
                                if (weldid.equals("")) {
                                    str1 = str1 + "0000000000000000";
                                    break;
                                } else {
                                    if (Integer.valueOf(weldid).toString().equals(StaticClass.listarrayJN.get(i + 2))) {
                                        welder1 = StaticClass.listarrayJN.get(i + 1);
                                        if (!isChinese(welder1)) {
                                            if (welder1 != "") {
                                                if (welder1.length() < 16) {
                                                    int length = 16 - welder1.length();
                                                    for (int j = 0; j < length; j++) {
                                                        welder1 = "0" + welder1;
                                                    }
                                                    str1 = str1 + welder1;
                                                    welder1 = "0000000000000000";
                                                    break;
                                                }
                                            } else {
                                                str1 = str1 + "0000000000000000";
                                            }
                                            break;
                                        }
                                    } else {
                                        count++;
                                        if (count == StaticClass.listarrayJN.size() / 5) {
                                            str1 = str1 + "0000000000000000";
                                            count = 0;
                                        }
                                    }
                                }
                            }
                        }

                        String time = elm.element("nowtime").getStringValue(); //时间
                        if (first1) {
                            System.out.println("Yes:" + time);
                            first1 = false;
                        }
//						String[] timebuf1 = time.split("/");
//						String[] timebuf2 = timebuf1[2].split(" ");
//						String[] timebuf3 = timebuf2[1].split(":");

                        Date date = new Date();
                        String nowtime = DateTools.format("YYYY/MM/dd HH:mm:ss", date);
                        String[] timebuf1 = nowtime.split("/");
                        String[] timebuf2 = timebuf1[2].split(" ");
                        String[] timebuf3 = timebuf2[1].split(":");

                        String year = Integer.toHexString(Integer.valueOf(timebuf1[0].substring(2, 4)));
                        if (year.length() < 2) {
                            int len = 2 - year.length();
                            for (int i = 0; i < len; i++) {
                                year = "0" + year;
                            }
                        }
                        String month = Integer.toHexString(Integer.valueOf(timebuf1[1]));
                        if (month.length() < 2) {
                            int len = 2 - month.length();
                            for (int i = 0; i < len; i++) {
                                month = "0" + month;
                            }
                        }
                        String day = Integer.toHexString(Integer.valueOf(timebuf2[0]));
                        if (day.length() < 2) {
                            int len = 2 - day.length();
                            for (int i = 0; i < len; i++) {
                                day = "0" + day;
                            }
                        }
                        String hour = Integer.toHexString(Integer.valueOf(timebuf3[0]));
                        if (hour.length() < 2) {
                            int len = 2 - hour.length();
                            for (int i = 0; i < len; i++) {
                                hour = "0" + hour;
                            }
                        }
                        String minute = Integer.toHexString(Integer.valueOf(timebuf3[1]));
                        if (minute.length() < 2) {
                            int len = 2 - minute.length();
                            for (int i = 0; i < len; i++) {
                                minute = "0" + minute;
                            }
                        }
                        String second = Integer.toHexString(Integer.valueOf(timebuf3[2]));
                        if (second.length() < 2) {
                            int len = 2 - second.length();
                            for (int i = 0; i < len; i++) {
                                second = "0" + second;
                            }
                        }
                        str1 = str1 + year + month + day + hour + minute + second;

                        String va = Integer.toHexString(Integer.valueOf(elm.element("wa").getStringValue())); //电流
                        if (va.length() < 4) {
                            int len = 4 - va.length();
                            for (int i = 0; i < len; i++) {
                                va = "0" + va;
                            }
                        }
                        str1 = str1 + va;


                        String vv = Integer.toHexString((int) (Double.valueOf(elm.element("wv").getStringValue()) * 10)); //电压
                        if (vv.length() < 4) {
                            int len = 4 - vv.length();
                            for (int i = 0; i < len; i++) {
                                vv = "0" + vv;
                            }
                        }
                        str1 = str1 + vv;
                        str1 = str1 + "0000";

                        String va1 = Integer.toHexString(Integer.valueOf(elm.element("va").getStringValue())); //电流
                        if (va1.length() < 4) {
                            int len = 4 - va1.length();
                            for (int i = 0; i < len; i++) {
                                va1 = "0" + va1;
                            }
                        }
                        str1 = str1 + va1;


                        String vv1 = Integer.toHexString((int) (Double.valueOf(elm.element("vv").getStringValue()) * 10)); //电压
                        if (vv1.length() < 4) {
                            int len = 4 - vv1.length();
                            for (int i = 0; i < len; i++) {
                                vv1 = "0" + vv1;
                            }
                        }
                        str1 = str1 + vv1;

                        if (StaticClass.listarrayJN.size() == 0) {             //设备编号对应任务信息得到焊口信息
                            str1 = str1 + "00000000";
                        } else {
                            String code = "00000000";
                            int counta = 0;
                            for (int i = 0; i < StaticClass.listarrayJN.size(); i += 5) {
                                if (weldid.equals("")) {
                                    str1 = str1 + "00000000";
                                    break;
                                } else {
                                    if (Integer.valueOf(weldid).toString().equals(StaticClass.listarrayJN.get(i + 2))) {
                                        code = StaticClass.listarrayJN.get(i);
                                        if (code != "") {
                                            if (code.length() < 8) {
                                                int length = 8 - code.length();
                                                for (int j = 0; j < length; j++) {
                                                    code = "0" + code;
                                                }
                                                str1 = str1 + code;
                                                code = "00000000";
                                                break;
                                            }
                                        } else {
                                            str1 = str1 + "00000000";
                                        }
                                        break;
                                    } else {
                                        counta++;
                                        if (counta == StaticClass.listarrayJN.size() / 5) {
                                            str1 = str1 + "00000000";
                                            counta = 0;
                                        }
                                    }
                                }
                            }
                        }

                        String statewarn = elm.element("wawv_alter").getStringValue();   //焊机报警状态判断
                        String state = elm.element("state").getStringValue();   //焊机状态

                        String channelbuf = Integer.toHexString(Integer.valueOf(elm.element("channel").getStringValue())); //通道
                        if (channelbuf.length() < 2) {
                            int len = 2 - channelbuf.length();
                            for (int i = 0; i < len; i++) {
                                channelbuf = "0" + channelbuf;
                            }
                        }

                        String wps = hm.get(nom + ":" + channelbuf);
                        String[] wpsde = null;
                        if (wps != null) {
                            wpsde = wps.split(",");
                            ;//判断焊机通道是否报警
                        }

                        //判断焊机报警
                        if (state.equals("待机")) {
                            str1 = str1 + "00";
                            //System.out.println("status:"+"00 "+"va:"+Integer.valueOf(va,16)+" vv:"+Integer.valueOf(vv,16)+" wpf:"+wps);
                        } else if (state.equals("焊接")) {
                            if (wpsde != null) {
                                if (Integer.valueOf(va, 16) > Integer.valueOf(wpsde[0]) || Integer.valueOf(va, 16) < Integer.valueOf(wpsde[1]) || Integer.valueOf(vv, 16) > Integer.valueOf(wpsde[2]) || Integer.valueOf(vv, 16) < Integer.valueOf(wpsde[3])) {
                                    str1 = str1 + "63";
                                    //System.out.println("status:"+"99 "+"va:"+Integer.valueOf(va,16)+" vv:"+Integer.valueOf(vv,16)+" wpf:"+wps);
                                } else {
                                    str1 = str1 + "03";
                                    //System.out.println("status:"+"03 "+"va:"+Integer.valueOf(va,16)+" vv:"+Integer.valueOf(vv,16)+" wpf:"+wps);
                                }
                            } else {
                                str1 = str1 + "03";
                                //System.out.println("status:"+"03 "+"va:"+Integer.valueOf(va,16)+" vv:"+Integer.valueOf(vv,16)+" wpf:"+wps);
                            }
                        } else {
                            str1 = str1 + "00";
                        }

                        //old
						/*if(statewarn.equals("0")){
				                String state = elm.element("state").getStringValue();   //焊机状态
				                if(state.equals("待机")){
				                	str1 = str1 + "00";
				                }else if(state.equals("焊接")){
				                	str1 = str1 + "03";
				                }else {
				                	str1 = str1 + "00";
				                }
			                }else if(statewarn.equals("1")){
			                	str1 = str1 + "63";
			                }else{
			                	String state = elm.element("state").getStringValue();   //焊机状态
				                if(state.equals("待机")){
				                	str1 = str1 + "00";
				                }else if(state.equals("焊接")){
				                	str1 = str1 + "03";
				                }else if(state.equals("报警")){
				                	str1 = str1 + "98";
				                }else {
				                	str1 = str1 + "00";
				                }
			                }*/

                        String wd = elm.element("wd").getStringValue();   //焊丝直径
                        if (wd.equals("0.6")) {
                            str1 = str1 + "06";
                        } else if (wd.equals("0.8")) {
                            str1 = str1 + "08";
                        } else if (wd.equals("0.9")) {
                            str1 = str1 + "09";
                        } else if (wd.equals("1.0")) {
                            str1 = str1 + "0A";
                        } else if (wd.equals("1.2")) {
                            str1 = str1 + "0C";
                        } else if (wd.equals("1.4")) {
                            str1 = str1 + "0E";
                        } else if (wd.equals("1.6")) {
                            str1 = str1 + "10";
                        } else {
                            str1 = str1 + "10";
                        }

                        int va_up = 0;
                        int vv_up = 0;
                        int va_down = 0;
                        int vv_down = 0;
                        try {
                            va_up = Integer.valueOf(wpsde[4]); //电流上限
                        } catch (Exception e) {
                            va_up = 200;
                        }
                        try {
                            vv_up = Integer.valueOf(wpsde[6]); //电压上限
                        } catch (Exception e) {
                            vv_up = 40;
                        }
                        try {
                            va_down = Integer.valueOf(wpsde[5]); //电流下限
                        } catch (Exception e) {
                            va_down = 20;
                        }
                        try {
                            vv_down = Integer.valueOf(wpsde[7]); //电压下限
                        } catch (Exception e) {
                            vv_down = 10;
                        }

                        if (va_down > va_up) {
                            str1 = str1 + "010000";
                        } else {
                            String vaset = Integer.toHexString((va_up + va_down) / 2);
                            if (vaset.length() < 4) {
                                int len = 4 - vaset.length();
                                for (int i = 0; i < len; i++) {
                                    vaset = "0" + vaset;
                                }
                            }
                            str1 = str1 + "01" + vaset;
                        }

                        if (vv_down > vv_up) {
                            str1 = str1 + "0000";
                        } else {
                            String vvset = Integer.toHexString((vv_up + vv_down) / 2);
                            if (vvset.length() < 4) {
                                int len = 4 - vvset.length();
                                for (int i = 0; i < len; i++) {
                                    vvset = "0" + vvset;
                                }
                            }
                            str1 = str1 + vvset;
                        }

                        if (va_down > va_up) {
                            str1 = str1 + "00";
                        } else {
                            String vaset = Integer.toHexString((va_up - va_down) / 2);
                            if (vaset.length() < 2) {
                                int len = 2 - vaset.length();
                                for (int i = 0; i < len; i++) {
                                    vaset = "0" + vaset;
                                }
                            }
                            str1 = str1 + vaset;
                        }

                        if (vv_down > vv_up) {
                            str1 = str1 + "00";
                        } else {
                            String vvset = Integer.toHexString((vv_up - vv_down) / 2);
                            if (vvset.length() < 2) {
                                int len = 2 - vvset.length();
                                for (int i = 0; i < len; i++) {
                                    vvset = "0" + vvset;
                                }
                            }
                            str1 = str1 + vvset;
                        }

                        str1 = str1 + "0000";

                        String channel = Integer.toHexString(Integer.valueOf(elm.element("channel").getStringValue())); //通道
                        if (channel.length() < 2) {
                            int len = 2 - channel.length();
                            for (int i = 0; i < len; i++) {
                                channel = "0" + channel;
                            }
                        }

                        int wa_up1 = 0;
                        int wv_up1 = 0;
                        int wa_down1 = 0;
                        int wv_down1 = 0;
                        try {
                            wa_up1 = Integer.valueOf(wpsde[0]); //电流上限
                        } catch (Exception e) {
                            wa_up1 = 500;
                        }
                        try {
                            wv_up1 = Integer.valueOf(wpsde[2]); //电压上限
                        } catch (Exception e) {
                            wv_up1 = 50;
                        }
                        try {
                            wa_down1 = Integer.valueOf(wpsde[1]); //电流下限
                        } catch (Exception e) {
                            wa_down1 = 10;
                        }
                        try {
                            wv_down1 = Integer.valueOf(wpsde[3]); //电压下限
                        } catch (Exception e) {
                            wv_down1 = 5;
                        }

                        String wa_up = Integer.toHexString(wa_up1); //报警电流上限
                        if (wa_up.length() < 4) {
                            int len = 4 - wa_up.length();
                            for (int i = 0; i < len; i++) {
                                wa_up = "0" + wa_up;
                            }
                        }
                        String wv_up = Integer.toHexString(wv_up1); //报警电压上限
                        if (wv_up.length() < 4) {
                            int len = 4 - wv_up.length();
                            for (int i = 0; i < len; i++) {
                                wv_up = "0" + wv_up;
                            }
                        }
                        String wa_down = Integer.toHexString(wa_down1); //报警电流下限
                        if (wa_down.length() < 4) {
                            int len = 4 - wa_down.length();
                            for (int i = 0; i < len; i++) {
                                wa_down = "0" + wa_down;
                            }
                        }
                        String wv_down = Integer.toHexString(wv_down1); //报警电压下限
                        if (wv_down.length() < 4) {
                            int len = 4 - wv_down.length();
                            for (int i = 0; i < len; i++) {
                                wv_down = "0" + wv_down;
                            }
                        }

                        str1 = str1 + channel + wa_up + wv_up + wa_down + wv_down + "00177D";
                        //dataView.append("松下:" + str1 + "\r\n");
                        if (str1.length() == 124) {
                            try {
                                if (StaticClass.chcli.isWritable()) {
                                    StaticClass.chcli.writeAndFlush(str1).sync();
                                }
                            } catch (Exception e) {
                                dataView.setText("wcf服务器未开启" + "\r\n");
                                e.printStackTrace();
                            }
                        }
                    }
                }

                Date dt4 = new Date();
                dataView.append("实时开始：" + DateTools.format("YY-MM-dd hh:mm:ss", dt1) + "\r\n");
                //dataView.append("实时调用wcf方法开始："+DateTools.format("YY-MM-DD hh:mm:ss", dt2) + "\r\n");
                //dataView.append("实时调用wcf方法结束："+DateTools.format("YY-MM-DD hh:mm:ss", dt3) + "\r\n");
                dataView.append("数据条数：" + Integer.toString(count1) + "\r\n");
                dataView.append("实时结束：" + DateTools.format("YY-MM-dd hh:mm:ss", dt4) + "\r\n");
                dataView.append("\r\n");
            }
        }

        public boolean isChinese(String str) {
            if (str == null) {
                return false;
            }
            for (char c : str.toCharArray()) {
                if (isChinese(c)) {
                    return true;
                }
            }
            return false;
        }

        public boolean isChinese(char c) {
            return c >= 0x4E00 && c <= 0x9FA3;
        }

    };

    public void tranpan(WeldServiceStub stu, HashMap<String, String> hm) {
        // TODO Auto-generated method stub
        this.stu = stu;
        this.hm = hm;
        new Thread(tranpanrun).start();
        //tranpanrun();
    }

    public class Workspace implements Runnable {

        private byte[] req;
        public String str = "";

        public Workspace(byte[] req) {
            // TODO Auto-generated constructor stub
            this.req = req;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {

                for (int i = 0; i < req.length; i++) {
                    //判断为数字还是字母，若为字母+256取正数
                    if (req[i] < 0) {
                        String r = Integer.toHexString(req[i] + 256);
                        String rr = r.toUpperCase();
                        //数字补为两位数
                        if (rr.length() == 1) {
                            rr = '0' + rr;
                        }
                        //strdata为总接收数据
                        str += rr;

                    } else {
                        String r = Integer.toHexString(req[i]);
                        if (r.length() == 1)
                            r = '0' + r;
                        r = r.toUpperCase();
                        str += r;
                    }
                }

                //chcli.writeAndFlush(str).sync();
                if (str.length() >= 6) {

                    if (str.substring(0, 2).equals("7E") && (str.substring(10, 12).equals("22")) && str.length() == 282) {

                        //System.out.println(str);
                        str = trans(str); //融合有无任务模式
                        //str = transOTC(str);
                        //str = transJN(str);
                        str = str.substring(0, 280) + fitemid + "7D";

                        try {
                            if (StaticClass.chcli.isWritable()) {
                                StaticClass.chcli.writeAndFlush(str).sync();
                            }


							/*String year = Integer.valueOf(str.subSequence(38, 40).toString(),16).toString();
		      	    		 String month = Integer.valueOf(str.subSequence(40, 42).toString(),16).toString();
		      	    		 String day = Integer.valueOf(str.subSequence(42, 44).toString(),16).toString();
		      	    		 String hour = Integer.valueOf(str.subSequence(44, 46).toString(),16).toString();
		      	    		 String minute = Integer.valueOf(str.subSequence(46, 48).toString(),16).toString();
		      	    		 String second = Integer.valueOf(str.subSequence(48, 50).toString(),16).toString();
		      	    		 String strdate = year+"-"+month+"-"+day+" "+hour+":"+minute+":"+second;
				        	 dataView.append(" 焊机：0001   电流:"+Integer.toString(Integer.valueOf(str.subSequence(50, 54).toString(),16))+"   电压："+Integer.toString(Integer.valueOf(str.subSequence(54, 58).toString(),16))+"   时间："+strdate+"\r\n");*/
                            dataView.append(" " + str + "\r\n");
                        } catch (Exception ex) {
                            //ex.printStackTrace();
                            dataView.setText("服务器未开启" + "\r\n");
                        }

                        str = "";

                    } else if (str.substring(0, 2).equals("FA")) {

                        str = str.substring(0, 106) + fitemid + "F5";

                        try {

                            StaticClass.chcli.writeAndFlush(str).sync();
                            dataView.append("实时:" + str + "\r\n");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            dataView.setText("服务器未开启" + "\r\n");
                        }

                        str = "";

                    } else {

                        try {
                            //str=str.substring(0,120)+fitemid+"7D";
                            if (StaticClass.chcli.isWritable()) {
                                StaticClass.chcli.writeAndFlush(str).sync();
                            }

                            dataView.append("上行:" + str + "\r\n");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            dataView.setText("服务器未开启" + "\r\n");
                        }

                        str = "";

                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                //dataView.append("数据接收错误" + "\r\n");
            }
        }


        private String trans(String str) {
            // TODO Auto-generated method stub

            if (str.length() == 282) {

                //校验第一位是否为FA末位是否为F5
                String check1 = str.substring(0, 2);
                String check11 = str.substring(280, 282);
                if (check1.equals("7E") && check11.equals("7D")) {

                    //校验位校验
                    String check3 = str.substring(0, 278);
                    String check5 = "";
                    int check4 = 0;
                    for (int i11 = 0; i11 < check3.length() / 2; i11++) {
                        String tstr1 = check3.substring(i11 * 2, i11 * 2 + 2);
                        check4 += Integer.valueOf(tstr1, 16);
                    }
                    if ((Integer.toHexString(check4)).toUpperCase().length() == 3) {
                        check5 = ((Integer.toHexString(check4)).toUpperCase()).substring(1, 3);
                    } else {
                        check5 = ((Integer.toHexString(check4)).toUpperCase()).substring(2, 4);
                    }
                    String check6 = str.substring(278, 280);
                    if (check6.equals(check6)) {

                        StringBuilder sb = new StringBuilder(str);
                        str = sb.toString();

                        String weld = str.substring(14, 18);
                        String welder = str.substring(34, 38);
                        String junction1 = str.substring(70, 78);
                        String junction2 = str.substring(150, 158);
                        String junction3 = str.substring(230, 238);

                        //江南任务模式
                        if (Integer.parseInt(welder, 16) == 0 && Integer.parseInt(junction1, 16) == 0 && Integer.parseInt(junction2, 16) == 0 && Integer.parseInt(junction3, 16) == 0) {

                            //焊机编号对应id
                            int countweld = 0;
                            String weldid = "";
                            if (StaticClass.listweld.size() == 0) {
                                sb.replace(14, 18, "0000");
                                sb.replace(18, 22, "0000");
                            } else {
                                for (int a = 0; a < StaticClass.listweld.size(); a += 4) {
                                    if (Integer.valueOf(StaticClass.listweld.get(a + 1)) == (Integer.parseInt(weld, 16))) {
                                        String gatherid = StaticClass.listweld.get(a);
                                        weldid = StaticClass.listweld.get(a + 2);

                                        if (gatherid.length() != 4) {
                                            int length = 4 - gatherid.length();
                                            for (int b = 0; b < length; b++) {
                                                gatherid = "0" + gatherid;
                                            }
                                        }

                                        if (weldid.length() != 4) {
                                            int length = 4 - weldid.length();
                                            for (int b = 0; b < length; b++) {
                                                weldid = "0" + weldid;
                                            }
                                        }

                                        sb.replace(14, 18, gatherid);
                                        sb.replace(18, 22, weldid);
                                        countweld = 0;

                                        break;
                                    } else {
                                        countweld++;
                                        if (countweld == StaticClass.listweld.size() / 4) {
                                            sb.replace(14, 18, "0000");
                                            sb.replace(18, 22, "0000");
                                            countweld = 0;
                                        }
                                    }
                                }
                            }

                            //江南任务下发,重置焊工id,焊口(任务id)
                            if (StaticClass.listarrayJN.size() == 0) {
                                sb.replace(34, 38, "0000");
                                sb.replace(70, 78, "00000000");
                                sb.replace(150, 158, "00000000");
                                sb.replace(230, 238, "00000000");
                            } else {
                                sb.replace(34, 38, "0000");
                                sb.replace(70, 78, "00000000");
                                sb.replace(150, 158, "00000000");
                                sb.replace(230, 238, "00000000");
                                String welder1 = "0000";
                                String code = "00000000";
                                for (int i = 0; i < StaticClass.listarrayJN.size(); i += 5) {
                                    if (weldid.equals("")) {
                                        welder1 = "0000";
                                        code = "00000000";
                                    } else {
                                        if (Integer.valueOf(weldid).toString().equals(StaticClass.listarrayJN.get(i + 2))) {
                                            welder1 = StaticClass.listarrayJN.get(i + 1);
                                            if (welder1 != "") {
                                                if (welder1.length() < 4) {
                                                    int length = 4 - welder1.length();
                                                    for (int j = 0; j < length; j++) {
                                                        welder1 = "0" + welder1;
                                                    }
                                                }
                                            } else {
                                                welder1 = "0000";
                                            }

                                            code = StaticClass.listarrayJN.get(i);
                                            if (code != "") {
                                                if (code.length() != 8) {
                                                    int length = 8 - code.length();
                                                    for (int i1 = 0; i1 < length; i1++) {
                                                        code = "0" + code;
                                                    }
                                                }
                                                code.toUpperCase();
                                            } else {
                                                code = "00000000";
                                            }
                                        }
                                    }
                                }
                                sb.replace(34, 38, welder1);
                                sb.replace(70, 78, code);
                                sb.replace(150, 158, code);
                                sb.replace(230, 238, code);
                            }

                            str = sb.toString();

                        } else {

                            //无任务下发模式
                            int countweld = 0;
                            int countwelder = 0;
                            int countjunction = 0;

                            //焊机编号对应id
                            if (StaticClass.listweld.size() == 0) {
                                sb.replace(14, 18, "0000");
                                sb.replace(18, 22, "0000");
                            } else {
                                for (int a = 0; a < StaticClass.listweld.size(); a += 4) {
                                    if (Integer.valueOf(StaticClass.listweld.get(a + 1)) == (Integer.parseInt(weld, 16))) {
                                        String gatherid = StaticClass.listweld.get(a);
                                        String weldid = StaticClass.listweld.get(a + 2);

                                        if (gatherid.length() != 4) {
                                            int length = 4 - gatherid.length();
                                            for (int b = 0; b < length; b++) {
                                                gatherid = "0" + gatherid;
                                            }
                                        }

                                        if (weldid.length() != 4) {
                                            int length = 4 - weldid.length();
                                            for (int b = 0; b < length; b++) {
                                                weldid = "0" + weldid;
                                            }
                                        }

                                        sb.replace(14, 18, gatherid);
                                        sb.replace(18, 22, weldid);
                                        countweld = 0;

                                    } else {
                                        countweld++;
                                        if (countweld == StaticClass.listweld.size() / 4) {
                                            sb.replace(14, 18, "0000");
                                            sb.replace(18, 22, "0000");
                                            countweld = 0;
                                        }
                                    }
                                }
                            }

                            //焊工编号对应id
                            if (StaticClass.listwelder.size() == 0) {
                                sb.replace(34, 38, "0000");
                            } else {
                                for (int a = 0; a < StaticClass.listwelder.size(); a += 2) {
                                    if (Integer.valueOf(StaticClass.listwelder.get(a + 1)) == (Integer.parseInt(welder, 16))) {
                                        String welderid = StaticClass.listwelder.get(a);

                                        if (welderid.length() != 4) {
                                            int length = 4 - welderid.length();
                                            for (int b = 0; b < length; b++) {
                                                welderid = "0" + welderid;
                                            }
                                        }

                                        sb.replace(34, 38, welderid);
                                        countwelder = 0;

                                    } else {
                                        countwelder++;
                                        if (countwelder == StaticClass.listwelder.size() / 2) {
                                            sb.replace(34, 38, "0000");
                                            countwelder = 0;
                                        }
                                    }
                                }
                            }

                            //焊口编号对应id(有三组数据的焊口)
                            if (StaticClass.listjunction.size() == 0) {
                                sb.replace(70, 78, "00000000");
                                sb.replace(150, 158, "00000000");
                                sb.replace(230, 238, "00000000");
                            } else {
                                for (int a = 0; a < StaticClass.listjunction.size(); a += 2) {
                                    if (Integer.valueOf(StaticClass.listjunction.get(a + 1)) == (Integer.parseInt(junction1, 16))) {
                                        String junctionid = StaticClass.listjunction.get(a);

                                        if (junctionid.length() != 8) {
                                            int length = 8 - junctionid.length();
                                            for (int b = 0; b < length; b++) {
                                                junctionid = "0" + junctionid;
                                            }
                                        }

                                        sb.replace(70, 78, junctionid);
                                        countjunction = 0;

                                    } else {
                                        countjunction++;
                                        if (countjunction == StaticClass.listjunction.size() / 2) {
                                            sb.replace(70, 78, "00000000");
                                            countjunction = 0;
                                        }
                                    }
                                }

                                for (int a = 0; a < StaticClass.listjunction.size(); a += 2) {
                                    if (Integer.valueOf(StaticClass.listjunction.get(a + 1)) == (Integer.parseInt(junction2, 16))) {
                                        String junctionid = StaticClass.listjunction.get(a);

                                        if (junctionid.length() != 8) {
                                            int length = 8 - junctionid.length();
                                            for (int b = 0; b < length; b++) {
                                                junctionid = "0" + junctionid;
                                            }
                                        }

                                        sb.replace(150, 158, junctionid);
                                        countjunction = 0;

                                    } else {
                                        countjunction++;
                                        if (countjunction == StaticClass.listjunction.size() / 2) {
                                            sb.replace(150, 158, "00000000");
                                            countjunction = 0;
                                        }
                                    }
                                }

                                for (int a = 0; a < StaticClass.listjunction.size(); a += 2) {
                                    if (Integer.valueOf(StaticClass.listjunction.get(a + 1)) == (Integer.parseInt(junction3, 16))) {
                                        String junctionid = StaticClass.listjunction.get(a);

                                        if (junctionid.length() != 8) {
                                            int length = 8 - junctionid.length();
                                            for (int b = 0; b < length; b++) {
                                                junctionid = "0" + junctionid;
                                            }
                                        }

                                        sb.replace(230, 238, junctionid);
                                        countjunction = 0;

                                    } else {
                                        countjunction++;
                                        if (countjunction == StaticClass.listjunction.size() / 2) {
                                            sb.replace(230, 238, "00000000");
                                            countjunction = 0;
                                        }
                                    }
                                }
                            }
                        }

                        str = sb.toString();

                    }
                }
            }
            return str;
        }

        private String transJN(String str) {
            // TODO Auto-generated method stub
            String strdata1 = str;
            String strdata2 = strdata1.replaceAll("7C20", "00");
            String strdata3 = strdata2.replaceAll("7C5E", "7E");
            String strdata4 = strdata3.replaceAll("7C5C", "7C");
            String strdata = strdata4.replaceAll("7C5D", "7D");

            //String weld = Integer.toString(Integer.valueOf(strdata.substring(2,4), 16));
            String weld1 = strdata.substring(6, 8);
            String weld2 = strdata.substring(2, 4);
            String weld = weld1 + weld2;
			/*String weld = Integer.toString(Integer.valueOf(weld1+weld2, 16));
            if(weld.length()<4){
            	int length = 4 - weld.length();
            	for(int i=0;i<length;i++){
            		weld = "0" + weld;
            	}
            }*/

            //江南任务下发
            String welder = "0000";
            String code = "00000000";
            for (int i = 0; i < StaticClass.listarrayJN.size(); i += 5) {
                if (weld.equals(StaticClass.listarrayJN.get(i + 4))) {
                    welder = StaticClass.listarrayJN.get(i + 1);
                    if (welder != "") {
                        welder = Integer.toHexString(Integer.valueOf(welder));
                        if (welder.length() < 4) {
                            int length = 4 - welder.length();
                            for (int j = 0; j < length; j++) {
                                welder = "0" + welder;
                            }
                        }
                    } else {
                        welder = "0000";
                    }

                    code = StaticClass.listarrayJN.get(i);
                    if (code != "") {
                        code = Integer.toHexString(Integer.valueOf(code));
                        if (code.length() != 8) {
                            int length = 8 - code.length();
                            for (int i1 = 0; i1 < length; i1++) {
                                code = "0" + code;
                            }
                        }
                        code.toUpperCase();
                    } else {
                        code = "00000000";
                    }
                }
            }

            String electricity1 = strdata.substring(28, 32);

            String electricity2 = strdata.substring(78, 82);

            String electricity3 = strdata.substring(128, 132);

            String voltage1 = strdata.substring(32, 36);

            String voltage2 = strdata.substring(82, 86);

            String voltage3 = strdata.substring(132, 136);

            //String code = strdata.substring(48,56);

            String status1 = strdata.substring(56, 58);

            String status2 = strdata.substring(106, 108);

            String status3 = strdata.substring(156, 158);

            timetran = new Date();
            timetran1 = timetran.getTime();
            time11 = new Date(timetran1);
            timetran2 = timetran1 + 1000;
            time22 = new Date(timetran2);
            timetran3 = timetran2 + 1000;
            time33 = new Date(timetran3);

            String time1 = DateTools.format("yyMMddHHmmss", time11);
            String time2 = DateTools.format("yyMMddHHmmss", time22);
            String time3 = DateTools.format("yyMMddHHmmss", time33);

            String year1 = time1.substring(0, 2);
            String year161 = Integer.toHexString(Integer.valueOf(year1));
            year161 = year161.toUpperCase();
            if (year161.length() == 1) {
                year161 = '0' + year161;
            }
            String month1 = time1.substring(2, 4);
            String month161 = Integer.toHexString(Integer.valueOf(month1));
            month161 = month161.toUpperCase();
            if (month161.length() == 1) {
                month161 = '0' + month161;
            }
            String day1 = time1.substring(4, 6);
            String day161 = Integer.toHexString(Integer.valueOf(day1));
            day161 = day161.toUpperCase();
            if (day161.length() == 1) {
                day161 = '0' + day161;
            }
            String hour1 = time1.substring(6, 8);
            String hour161 = Integer.toHexString(Integer.valueOf(hour1));
            hour161 = hour161.toUpperCase();
            if (hour161.length() == 1) {
                hour161 = '0' + hour161;
            }
            String minute1 = time1.substring(8, 10);
            String minute161 = Integer.toHexString(Integer.valueOf(minute1));
            minute161 = minute161.toUpperCase();
            if (minute161.length() == 1) {
                minute161 = '0' + minute161;
            }
            String second1 = time1.substring(10, 12);
            String second161 = Integer.toHexString(Integer.valueOf(second1));
            second161 = second161.toUpperCase();
            if (second161.length() == 1) {
                second161 = '0' + second161;
            }

            String year2 = time2.substring(0, 2);
            String year162 = Integer.toHexString(Integer.valueOf(year2));
            year162 = year162.toUpperCase();
            if (year162.length() == 1) {
                year162 = '0' + year162;
            }
            String month2 = time2.substring(2, 4);
            String month162 = Integer.toHexString(Integer.valueOf(month2));
            month162 = month162.toUpperCase();
            if (month162.length() == 1) {
                month162 = '0' + month162;
            }
            String day2 = time2.substring(4, 6);
            String day162 = Integer.toHexString(Integer.valueOf(day2));
            day162 = day162.toUpperCase();
            if (day162.length() == 1) {
                day162 = '0' + day162;
            }
            String hour2 = time2.substring(6, 8);
            String hour162 = Integer.toHexString(Integer.valueOf(hour2));
            hour162 = hour162.toUpperCase();
            if (hour162.length() == 1) {
                hour162 = '0' + hour162;
            }
            String minute2 = time2.substring(8, 10);
            String minute162 = Integer.toHexString(Integer.valueOf(minute2));
            minute162 = minute162.toUpperCase();
            if (minute162.length() == 1) {
                minute162 = '0' + minute162;
            }
            String second2 = time2.substring(10, 12);
            String second162 = Integer.toHexString(Integer.valueOf(second2));
            second162 = second162.toUpperCase();
            if (second162.length() == 1) {
                second162 = '0' + second162;
            }

            String year3 = time3.substring(0, 2);
            String year163 = Integer.toHexString(Integer.valueOf(year3));
            year163 = year163.toUpperCase();
            if (year163.length() == 1) {
                year163 = '0' + year163;
            }
            String month3 = time3.substring(2, 4);
            String month163 = Integer.toHexString(Integer.valueOf(month3));
            month163 = month163.toUpperCase();
            if (month163.length() == 1) {
                month163 = '0' + month163;
            }
            String day3 = time3.substring(4, 6);
            String day163 = Integer.toHexString(Integer.valueOf(day3));
            day163 = day163.toUpperCase();
            if (day163.length() == 1) {
                day163 = '0' + day163;
            }
            String hour3 = time3.substring(6, 8);
            String hour163 = Integer.toHexString(Integer.valueOf(hour3));
            hour163 = hour163.toUpperCase();
            if (hour163.length() == 1) {
                hour163 = '0' + hour163;
            }
            String minute3 = time3.substring(8, 10);
            String minute163 = Integer.toHexString(Integer.valueOf(minute3));
            minute163 = minute163.toUpperCase();
            if (minute163.length() == 1) {
                minute163 = '0' + minute163;
            }
            String second3 = time3.substring(10, 12);
            String second163 = Integer.toHexString(Integer.valueOf(second3));
            second163 = second163.toUpperCase();
            if (second163.length() == 1) {
                second163 = '0' + second163;
            }

            String datesend = "00003101" + weld + welder + code
                    + electricity1 + voltage1 + "0000" + status1 + year161 + month161 + day161 + hour161 + minute161 + second161
                    + electricity2 + voltage2 + "0000" + status2 + year162 + month162 + day162 + hour162 + minute162 + second162
                    + electricity3 + voltage3 + "0000" + status3 + year163 + month163 + day163 + hour163 + minute163 + second163;

            int check = 0;
            byte[] data1 = new byte[datesend.length() / 2];
            for (int i = 0; i < data1.length; i++) {
                String tstr1 = datesend.substring(i * 2, i * 2 + 2);
                Integer k = Integer.valueOf(tstr1, 16);
                check += k;
            }

            String checksend = Integer.toHexString(check);
            int a = checksend.length();
            checksend = checksend.substring(a - 2, a);
            checksend = checksend.toUpperCase();

            datesend = "FA" + datesend + checksend + "F5";
            datesend = datesend.toUpperCase();

            return datesend;
        }

        private String transOTC(String str) {
            // TODO Auto-generated method stub
            String strdata1 = str;
            String strdata2 = strdata1.replaceAll("7C20", "00");
            String strdata3 = strdata2.replaceAll("7C5E", "7E");
            String strdata4 = strdata3.replaceAll("7C5C", "7C");
            String strdata = strdata4.replaceAll("7C5D", "7D");

            String weld1 = strdata.substring(6, 8);
            String weld2 = strdata.substring(2, 4);
            String weld = weld1 + weld2;
            if (weld.length() < 4) {
                int length = 4 - weld.length();
                for (int i = 0; i < length; i++) {
                    weld = "0" + weld;
                }
            }

            String welder1 = Integer.valueOf(strdata.substring(20, 22), 16).toString();
            String welder2 = Integer.valueOf(strdata.substring(22, 24), 16).toString();
            String welder3 = Integer.valueOf(strdata.substring(24, 26), 16).toString();
            String welder4 = Integer.valueOf(strdata.substring(26, 28), 16).toString();
            String welder = welder1 + "," + welder2 + "," + welder3 + "," + welder4;
            StringBuffer sbu = new StringBuffer();
            String[] chars = welder.split(",");
            for (int i = 0; i < chars.length; i++) {
                sbu.append((char) Integer.parseInt(chars[i]));
            }
            welder = Integer.toHexString(Integer.valueOf(sbu.toString()));
            if (welder.length() != 4) {
                int lenth = 4 - welder.length();
                for (int i = 0; i < lenth; i++) {
                    welder = "0" + welder;
                }
            }

            String electricity1 = strdata.substring(28, 32);

            String electricity2 = strdata.substring(78, 82);

            String electricity3 = strdata.substring(128, 132);

            String voltage1 = strdata.substring(32, 36);

            String voltage2 = strdata.substring(82, 86);

            String voltage3 = strdata.substring(132, 136);

            String code = strdata.substring(48, 56);

            String status1 = strdata.substring(56, 58);

            String status2 = strdata.substring(106, 108);

            String status3 = strdata.substring(156, 158);

			/*if(First){
            	timetran = new Date();
            	timetran1 = timetran.getTime();
                time11 = new Date(timetran1);
                timetran2 = timetran1 + 1000;
                time22 = new Date(timetran2);
                timetran3 = timetran2 + 1000;
                time33 = new Date(timetran3);
                timetran1 = timetran3;
            	First = false;
            }else{
            	timetran1 = timetran1 + 1000;
                time11 = new Date(timetran1);
            	timetran2 = timetran1 + 1000;
                time22 = new Date(timetran2);
                timetran3 = timetran2 + 1000;
                time33 = new Date(timetran3);
                timetran1 = timetran3;
            }*/

            timetran = new Date();
            timetran1 = timetran.getTime();
            time11 = new Date(timetran1);
            timetran2 = timetran1 + 1000;
            time22 = new Date(timetran2);
            timetran3 = timetran2 + 1000;
            time33 = new Date(timetran3);

            String time1 = DateTools.format("yyMMddHHmmss", time11);
            String time2 = DateTools.format("yyMMddHHmmss", time22);
            String time3 = DateTools.format("yyMMddHHmmss", time33);

            String year1 = time1.substring(0, 2);
            String year161 = Integer.toHexString(Integer.valueOf(year1));
            year161 = year161.toUpperCase();
            if (year161.length() == 1) {
                year161 = '0' + year161;
            }
            String month1 = time1.substring(2, 4);
            String month161 = Integer.toHexString(Integer.valueOf(month1));
            month161 = month161.toUpperCase();
            if (month161.length() == 1) {
                month161 = '0' + month161;
            }
            String day1 = time1.substring(4, 6);
            String day161 = Integer.toHexString(Integer.valueOf(day1));
            day161 = day161.toUpperCase();
            if (day161.length() == 1) {
                day161 = '0' + day161;
            }
            String hour1 = time1.substring(6, 8);
            String hour161 = Integer.toHexString(Integer.valueOf(hour1));
            hour161 = hour161.toUpperCase();
            if (hour161.length() == 1) {
                hour161 = '0' + hour161;
            }
            String minute1 = time1.substring(8, 10);
            String minute161 = Integer.toHexString(Integer.valueOf(minute1));
            minute161 = minute161.toUpperCase();
            if (minute161.length() == 1) {
                minute161 = '0' + minute161;
            }
            String second1 = time1.substring(10, 12);
            String second161 = Integer.toHexString(Integer.valueOf(second1));
            second161 = second161.toUpperCase();
            if (second161.length() == 1) {
                second161 = '0' + second161;
            }

            String year2 = time2.substring(0, 2);
            String year162 = Integer.toHexString(Integer.valueOf(year2));
            year162 = year162.toUpperCase();
            if (year162.length() == 1) {
                year162 = '0' + year162;
            }
            String month2 = time2.substring(2, 4);
            String month162 = Integer.toHexString(Integer.valueOf(month2));
            month162 = month162.toUpperCase();
            if (month162.length() == 1) {
                month162 = '0' + month162;
            }
            String day2 = time2.substring(4, 6);
            String day162 = Integer.toHexString(Integer.valueOf(day2));
            day162 = day162.toUpperCase();
            if (day162.length() == 1) {
                day162 = '0' + day162;
            }
            String hour2 = time2.substring(6, 8);
            String hour162 = Integer.toHexString(Integer.valueOf(hour2));
            hour162 = hour162.toUpperCase();
            if (hour162.length() == 1) {
                hour162 = '0' + hour162;
            }
            String minute2 = time2.substring(8, 10);
            String minute162 = Integer.toHexString(Integer.valueOf(minute2));
            minute162 = minute162.toUpperCase();
            if (minute162.length() == 1) {
                minute162 = '0' + minute162;
            }
            String second2 = time2.substring(10, 12);
            String second162 = Integer.toHexString(Integer.valueOf(second2));
            second162 = second162.toUpperCase();
            if (second162.length() == 1) {
                second162 = '0' + second162;
            }

            String year3 = time3.substring(0, 2);
            String year163 = Integer.toHexString(Integer.valueOf(year3));
            year163 = year163.toUpperCase();
            if (year163.length() == 1) {
                year163 = '0' + year163;
            }
            String month3 = time3.substring(2, 4);
            String month163 = Integer.toHexString(Integer.valueOf(month3));
            month163 = month163.toUpperCase();
            if (month163.length() == 1) {
                month163 = '0' + month163;
            }
            String day3 = time3.substring(4, 6);
            String day163 = Integer.toHexString(Integer.valueOf(day3));
            day163 = day163.toUpperCase();
            if (day163.length() == 1) {
                day163 = '0' + day163;
            }
            String hour3 = time3.substring(6, 8);
            String hour163 = Integer.toHexString(Integer.valueOf(hour3));
            hour163 = hour163.toUpperCase();
            if (hour163.length() == 1) {
                hour163 = '0' + hour163;
            }
            String minute3 = time3.substring(8, 10);
            String minute163 = Integer.toHexString(Integer.valueOf(minute3));
            minute163 = minute163.toUpperCase();
            if (minute163.length() == 1) {
                minute163 = '0' + minute163;
            }
            String second3 = time3.substring(10, 12);
            String second163 = Integer.toHexString(Integer.valueOf(second3));
            second163 = second163.toUpperCase();
            if (second163.length() == 1) {
                second163 = '0' + second163;
            }

			/*String status1 = "00";
            String status2 = "00";
            String status3 = "00";
            if(electricityint1 != 0){
            	status1 = "03";
            }else if(electricityint2 != 0){
            	status2 = "03";
            }else if(electricityint3 != 0){
            	status3 = "03";
            }*/

            String datesend = "00003101" + weld + welder + code
                    + electricity1 + voltage1 + "0000" + status1 + year161 + month161 + day161 + hour161 + minute161 + second161
                    + electricity2 + voltage2 + "0000" + status2 + year162 + month162 + day162 + hour162 + minute162 + second162
                    + electricity3 + voltage3 + "0000" + status3 + year163 + month163 + day163 + hour163 + minute163 + second163;

            int check = 0;
            byte[] data1 = new byte[datesend.length() / 2];
            for (int i = 0; i < data1.length; i++) {
                String tstr1 = datesend.substring(i * 2, i * 2 + 2);
                Integer k = Integer.valueOf(tstr1, 16);
                check += k;
            }

            String checksend = Integer.toHexString(check);
            int a = checksend.length();
            checksend = checksend.substring(a - 2, a);
            checksend = checksend.toUpperCase();

            datesend = "FA" + datesend + checksend + "F5";
            datesend = datesend.toUpperCase();

            return datesend;
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
        System.out.println("新增连接：" + clientIp + ":" + clientPort);
        String address = clientIp + ":" + clientPort;
        if (MAP.size() > 0) {
            a:
            for (JSONArray array : MAP.values()) {
                for (Object o : array) {
                    JSONObject jsonObject = (JSONObject) o;
                    //通道id存在，就更新掉开机时间
                    if (address.equals(jsonObject.getString("address"))) {
                        jsonObject.put("startTime", sdf.format(System.currentTimeMillis()));
                        System.out.println("开机时间已更新：" + address);
                        break a;
                    }
                }
            }
        }
    }

    /**
     * @param ctx
     * @description: 有客户端终止连接服务器会触发此函数
     * @return: void
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        int clientPort = insocket.getPort();
        ChannelId channelId = ctx.channel().id();
        String address = clientIp + ":" + clientPort;
        //包含此客户端才去删除
        if (MAP.size() > 0) {
            a:
            for (JSONArray array : MAP.values()) {
                for (Object o : array) {
                    JSONObject jsonObject = (JSONObject) o;
                    //通道id存在，就更新掉关机时间
                    if (address.equals(jsonObject.getString("address"))) {
                        jsonObject.put("offTime", sdf.format(System.currentTimeMillis()));
                        System.out.println("关机时间已更新：" + address);
                        break a;
                    }
                }
            }
        }
        System.out.println("终止连接：" + clientIp + ":" + clientPort);
        ctx.channel().close();
        ctx.close();
    }

    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常：" + cause);
        ctx.close();
    }

}
