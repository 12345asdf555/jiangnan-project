package com;


import java.math.BigDecimal;
import java.sql.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DB_Connectionmysql {

    public String select;
    public String datasend = "";
    public String fmachine;
    public String connet;
    public ArrayList<String> listarray1 = new ArrayList<String>();
    public ArrayList<String> listarray2 = new ArrayList<String>();
    public ArrayList<String> listarray3 = new ArrayList<String>();

    public String getId() {
        return datasend;
    }

    public void setId(String datasend) {
        this.datasend = datasend;
    }

    public Server server;
    public java.sql.Connection conn = null;
    public java.sql.Statement stmt = null;

    public int ceng = 0;
    public int dao = 0;
    public int weldstatus = 0;

    public int work = 1;
    public int count1 = 1;
    public int count2 = 1;
    public int count3 = 1;
    public int count4 = 1;
    public String inSql1 = "";
    public String inSql2 = "";
    public String inSql3 = "";
    public String inSql4 = "";
    public final String inSql = "insert into tb_live_data(felectricity,fvoltage,frateofflow,fgather_no,fwelder_id,fjunction_id,fstatus,fitemid,FUploadDateTime,FWeldTime,fmachine_id) values";

    public int workbase = 1;
    public int countbase1 = 1;
    public int countbase2 = 1;
    public int countbase3 = 1;
    public int countbase4 = 1;
    public String inSqlbase1 = "";
    public String inSqlbase2 = "";
    public String inSqlbase3 = "";
    public String inSqlbase4 = "";
    public final String inSqlbase = "insert into tb_live_data(fwelder_id,fgather_no,fmachine_id,fjunction_id,fitemid,felectricity,fvoltage,fstatus,fwirefeedrate,FUploadDateTime,FWeldTime,fwelder_no,fjunction_no,fweld_no,fchannel,fmax_electricity,fmin_electricity,fmax_voltage,fmin_voltage,fwelder_itemid,fjunction_itemid,fmachine_itemid,fmachinemodel,fwirediameter,fmaterialgas,fwmax_electricity,fwmin_electricity,fwmax_voltage,fwmin_voltage,fsolder_layer,fweld_bead) values";


    public DB_Connectionmysql() {
    }

    /**
     * sql执行
     */
    private void executeSQL(String sql) {
        Server.cachedThreadPool.execute(new SqlWorkInsert(sql));
    }

    private static class SqlWorkInsert implements Runnable {
        private String sql = "";

        public SqlWorkInsert(String sql) {
            this.sql = sql;
        }

        @Override
        public void run() {
            if (null != sql && !"".equals(sql)) {
                Connection connection = null;
                Statement statement = null;
                try {
                    connection = Server.dbConnection.getConnection();
                    statement = connection.createStatement();
                    synchronized (sql) {
                        statement.executeUpdate(sql);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    //释放连接，归还资源
                    Server.dbConnection.close(connection, statement, null);
                }
            }
        }
    }

    public void DB_Connectionmysqlrun(long welderid, long weldid, long gatherid, long itemid, long weldid2, String weldmodel, long junctionid, BigDecimal electricity, BigDecimal voltage, int status, BigDecimal fwirefeedrate, Timestamp timesql, int channel, BigDecimal maxelectricity, BigDecimal minelectricity, BigDecimal maxvoltage, BigDecimal minvoltage, BigDecimal fwirediameter, int fmaterialgas, ArrayList<String> listarray1, ArrayList<String> listarray2, ArrayList<String> listarray3, BigDecimal wmaxelectricity, BigDecimal wminelectricity, BigDecimal wmaxvoltage, BigDecimal wminvoltage) {
        Date date;
        String nowTime;
        Timestamp goodsC_date;
        String weldernum = "0000";
        String welderins = "00";
        String junctionnum = "0000";
        String junctionins = "00";
        String gathernum = "0000";
        String weldnum = "0000";
        String ins = "00";

        if (weldstatus == 0) {     //焊层焊道不为停止

            switch (workbase) {
                case 1:
                    date = new Date();
                    nowTime = DateTools.format("yyyy-MM-dd HH:mm:ss", date);
                    goodsC_date = Timestamp.valueOf(nowTime);

                    BigDecimal voltage1 = new BigDecimal(((double) Integer.valueOf(voltage.toString())) / 10);
                    BigDecimal maxvoltage1 = new BigDecimal(((double) Integer.valueOf(maxvoltage.toString())) / 10);
                    BigDecimal minvoltage1 = new BigDecimal(((double) Integer.valueOf(minvoltage.toString())) / 10);
                    BigDecimal wmaxvoltage1 = new BigDecimal(((double) Integer.valueOf(wmaxvoltage.toString())) / 10);
                    BigDecimal wminvoltage1 = new BigDecimal(((double) Integer.valueOf(wminvoltage.toString())) / 10);

                    for (int a = 0; a < listarray1.size(); a += 3) {
                        if (welderid == Integer.valueOf(listarray1.get(a))) {
                            weldernum = listarray1.get(a + 1);
                            welderins = listarray1.get(a + 2);
                            break;
                        }
                    }

                    for (int a = 0; a < listarray3.size(); a += 7) {
                        if (junctionid == Integer.valueOf(listarray3.get(a + 5))) {
                            junctionnum = listarray3.get(a);
                            junctionins = listarray3.get(a + 6);
                            break;
                        }
                    }

                    for (int a = 0; a < listarray2.size(); a += 4) {
                        if (gatherid == Long.parseLong(listarray2.get(a+2))) {
                            gathernum = listarray2.get(a + 2);
                            weldnum = listarray2.get(a + 1);
                            ins = listarray2.get(a + 3);
                            break;
                        }
                    }

                    if (ins == null || ins.equals("null")) {
                        ins = "00";
                    }
                    if (junctionins == null || junctionins.equals(null) || junctionins.equals("null")) {
                        junctionins = "00";
                    }
                    if (junctionins == null || welderins.equals(null) || welderins.equals("null")) {
                        welderins = "00";
                    }

                    if (countbase1 == 1) {
                        inSqlbase1 = inSqlbase + "('" + welderid + "','" + gathernum + "','" + weldid + "','" + junctionid + "','" + itemid + "','" + electricity + "','" + voltage1 + "','" + status + "','" + fwirefeedrate + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage1 + "','" + minvoltage1 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "','" + wmaxelectricity + "','" + wminelectricity + "','" + wmaxvoltage1 + "','" + wminvoltage1 + "','" + ceng + "','" + dao + "')";
                    } else {
                        inSqlbase1 = inSqlbase1 + ",('" + welderid + "','" + gathernum + "','" + weldid + "','" + junctionid + "','" + itemid + "','" + electricity + "','" + voltage1 + "','" + status + "','" + fwirefeedrate + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage1 + "','" + minvoltage1 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "','" + wmaxelectricity + "','" + wminelectricity + "','" + wmaxvoltage1 + "','" + wminvoltage1 + "','" + ceng + "','" + dao + "')";
                    }

                    countbase1++;

                    if (countbase1 >= 100) {
                        executeSQL(inSqlbase1);
                        workbase = workbase + 1;
                        if (workbase == 5) {
                            workbase = 1;
                        }
                        countbase1 = 1;
                        inSqlbase1 = "";
                    }
                    break;
                case 2:
                    date = new Date();
                    nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                    goodsC_date = Timestamp.valueOf(nowTime);

                    BigDecimal voltage2 = new BigDecimal(((double) Integer.valueOf(voltage.toString())) / 10);
                    BigDecimal maxvoltage2 = new BigDecimal(((double) Integer.valueOf(maxvoltage.toString())) / 10);
                    BigDecimal minvoltage2 = new BigDecimal(((double) Integer.valueOf(minvoltage.toString())) / 10);
                    BigDecimal wmaxvoltage2 = new BigDecimal(((double) Integer.valueOf(wmaxvoltage.toString())) / 10);
                    BigDecimal wminvoltage2 = new BigDecimal(((double) Integer.valueOf(wminvoltage.toString())) / 10);

                    for (int a = 0; a < listarray1.size(); a += 3) {
                        if (welderid == Integer.valueOf(listarray1.get(a))) {
                            weldernum = listarray1.get(a + 1);
                            welderins = listarray1.get(a + 2);
                            break;
                        }
                    }

                    for (int a = 0; a < listarray3.size(); a += 7) {
                        if (junctionid == Integer.valueOf(listarray3.get(a + 5))) {
                            junctionnum = listarray3.get(a);
                            junctionins = listarray3.get(a + 6);
                            break;
                        }
                    }

                    for (int a = 0; a < listarray2.size(); a += 4) {
                        if (gatherid == Long.parseLong(listarray2.get(a+2))) {
                            gathernum = listarray2.get(a + 2);
                            weldnum = listarray2.get(a + 1);
                            ins = listarray2.get(a + 3);
                            break;
                        }
                    }

                    if (ins == null || ins.equals("null")) {
                        ins = "00";
                    } else if (junctionins == null || junctionins.equals(null) || junctionins.equals("null")) {
                        junctionins = "00";
                    } else if (welderins == null || welderins.equals(null) || welderins.equals("null")) {
                        welderins = "00";
                    }

                    if (countbase2 == 1) {
                        inSqlbase2 = inSqlbase + "('" + welderid + "','" + gathernum + "','" + weldid + "','" + junctionid + "','" + itemid + "','" + electricity + "','" + voltage2 + "','" + status + "','" + fwirefeedrate + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage2 + "','" + minvoltage2 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "','" + wmaxelectricity + "','" + wminelectricity + "','" + wmaxvoltage2 + "','" + wminvoltage2 + "','" + ceng + "','" + dao + "')";
                    } else {
                        inSqlbase2 = inSqlbase2 + ",('" + welderid + "','" + gathernum + "','" + weldid + "','" + junctionid + "','" + itemid + "','" + electricity + "','" + voltage2 + "','" + status + "','" + fwirefeedrate + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage2 + "','" + minvoltage2 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "','" + wmaxelectricity + "','" + wminelectricity + "','" + wmaxvoltage2 + "','" + wminvoltage2 + "','" + ceng + "','" + dao + "')";
                    }

                    countbase2++;

                    if (countbase2 >= 100) {
                        executeSQL(inSqlbase2);
                        workbase = workbase + 1;
                        if (workbase == 5) {
                            workbase = 1;
                        }
                        countbase2 = 1;
                        inSqlbase2 = "";
                    }
                    break;
                case 3:
                    date = new Date();
                    nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                    goodsC_date = Timestamp.valueOf(nowTime);

                    BigDecimal voltage3 = new BigDecimal(((double) Integer.valueOf(voltage.toString())) / 10);
                    BigDecimal maxvoltage3 = new BigDecimal(((double) Integer.valueOf(maxvoltage.toString())) / 10);
                    BigDecimal minvoltage3 = new BigDecimal(((double) Integer.valueOf(minvoltage.toString())) / 10);
                    BigDecimal wmaxvoltage3 = new BigDecimal(((double) Integer.valueOf(wmaxvoltage.toString())) / 10);
                    BigDecimal wminvoltage3 = new BigDecimal(((double) Integer.valueOf(wminvoltage.toString())) / 10);

                    for (int a = 0; a < listarray1.size(); a += 3) {
                        if (welderid == Integer.valueOf(listarray1.get(a))) {
                            weldernum = listarray1.get(a + 1);
                            welderins = listarray1.get(a + 2);
                        }
                    }

                    for (int a = 0; a < listarray3.size(); a += 7) {
                        if (junctionid == Integer.valueOf(listarray3.get(a + 5))) {
                            junctionnum = listarray3.get(a);
                            junctionins = listarray3.get(a + 6);
                        }
                    }

                    for (int a = 0; a < listarray2.size(); a += 4) {
                        if (gatherid == Long.parseLong(listarray2.get(a+2))) {
                            gathernum = listarray2.get(a + 2);
                            weldnum = listarray2.get(a + 1);
                            ins = listarray2.get(a + 3);
                        }
                    }

                    if (ins == null || ins.equals("null")) {
                        ins = "00";
                    } else if (junctionins == null || junctionins.equals(null) || junctionins.equals("null")) {
                        junctionins = "00";
                    } else if (welderins == null || welderins.equals(null) || welderins.equals("null")) {
                        welderins = "00";
                    }

                    if (countbase3 == 1) {
                        inSqlbase3 = inSqlbase + "('" + welderid + "','" + gathernum + "','" + weldid + "','" + junctionid + "','" + itemid + "','" + electricity + "','" + voltage3 + "','" + status + "','" + fwirefeedrate + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage3 + "','" + minvoltage3 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "','" + wmaxelectricity + "','" + wminelectricity + "','" + wmaxvoltage3 + "','" + wminvoltage3 + "','" + ceng + "','" + dao + "')";
                    } else {
                        inSqlbase3 = inSqlbase3 + ",('" + welderid + "','" + gathernum + "','" + weldid + "','" + junctionid + "','" + itemid + "','" + electricity + "','" + voltage3 + "','" + status + "','" + fwirefeedrate + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage3 + "','" + minvoltage3 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "','" + wmaxelectricity + "','" + wminelectricity + "','" + wmaxvoltage3 + "','" + wminvoltage3 + "','" + ceng + "','" + dao + "')";
                    }

                    countbase3++;

                    if (countbase3 >= 100) {
                        executeSQL(inSqlbase3);
                        workbase = workbase + 1;
                        if (workbase == 5) {
                            workbase = 1;
                        }
                        countbase3 = 1;
                        inSqlbase3 = "";
                    }
                    break;
                case 4:
                    date = new Date();
                    nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                    goodsC_date = Timestamp.valueOf(nowTime);

                    BigDecimal voltage4 = new BigDecimal(((double) Integer.valueOf(voltage.toString())) / 10);
                    BigDecimal maxvoltage4 = new BigDecimal(((double) Integer.valueOf(maxvoltage.toString())) / 10);
                    BigDecimal minvoltage4 = new BigDecimal(((double) Integer.valueOf(minvoltage.toString())) / 10);
                    BigDecimal wmaxvoltage4 = new BigDecimal(((double) Integer.valueOf(wmaxvoltage.toString())) / 10);
                    BigDecimal wminvoltage4 = new BigDecimal(((double) Integer.valueOf(wminvoltage.toString())) / 10);

                    for (int a = 0; a < listarray1.size(); a += 3) {
                        if (welderid == Integer.valueOf(listarray1.get(a))) {
                            weldernum = listarray1.get(a + 1);
                            welderins = listarray1.get(a + 2);
                        }
                    }

                    for (int a = 0; a < listarray3.size(); a += 7) {
                        if (junctionid == Integer.valueOf(listarray3.get(a + 5))) {
                            junctionnum = listarray3.get(a);
                            junctionins = listarray3.get(a + 6);
                        }
                    }

                    for (int a = 0; a < listarray2.size(); a += 4) {
                        if (gatherid == Long.parseLong(listarray2.get(a+2))) {
                            gathernum = listarray2.get(a + 2);
                            weldnum = listarray2.get(a + 1);
                            ins = listarray2.get(a + 3);
                        }
                    }

                    if (ins == null || ins.equals("null")) {
                        ins = "00";
                    } else if (junctionins == null || junctionins.equals(null) || junctionins.equals("null")) {
                        junctionins = "00";
                    } else if (welderins == null || welderins.equals(null) || welderins.equals("null")) {
                        welderins = "00";
                    }

                    if (countbase4 == 1) {
                        inSqlbase4 = inSqlbase + "('" + welderid + "','" + gathernum + "','" + weldid + "','" + junctionid + "','" + itemid + "','" + electricity + "','" + voltage4 + "','" + status + "','" + fwirefeedrate + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage4 + "','" + minvoltage4 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "','" + wmaxelectricity + "','" + wminelectricity + "','" + wmaxvoltage4 + "','" + wminvoltage4 + "','" + ceng + "','" + dao + "')";
                    } else {
                        inSqlbase4 = inSqlbase4 + ",('" + welderid + "','" + gathernum + "','" + weldid + "','" + junctionid + "','" + itemid + "','" + electricity + "','" + voltage4 + "','" + status + "','" + fwirefeedrate + "','" + goodsC_date + "','" + timesql + "','" + weldernum + "','" + junctionnum + "','" + weldnum + "','" + channel + "','" + maxelectricity + "','" + minelectricity + "','" + maxvoltage4 + "','" + minvoltage4 + "','" + welderins + "','" + junctionins + "','" + ins + "','" + weldmodel + "','" + fwirediameter + "','" + fmaterialgas + "','" + wmaxelectricity + "','" + wminelectricity + "','" + wmaxvoltage4 + "','" + wminvoltage4 + "','" + ceng + "','" + dao + "')";
                    }

                    countbase4++;

                    if (countbase4 >= 100) {
                        executeSQL(inSqlbase4);
                        workbase = workbase + 1;
                        if (workbase == 5) {
                            workbase = 1;
                        }
                        countbase4 = 1;
                        inSqlbase4 = "";
                    }
                    break;
            }
        }
    }

    public void DB_Connectionmysqlrun(BigDecimal electricity, BigDecimal voltage, String sensor_Num, String machine_id, String welder_id, String code, int status, String fitemid, Timestamp timesql, ArrayList<String> listarray1, ArrayList<String> listarray2, ArrayList<String> listarray3) {

        Date date;
        String nowTime;
        Timestamp goodsC_date;
        String welder = "0";
        String junctionnum = "0";
        String fmachine = null;
        switch (work) {
            case 1:
                date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);

                for (int i = 0; i < listarray1.size(); i += 2) {
                    if (welder_id.equals(listarray1.get(i))) {
                        welder = listarray1.get(i + 1);
                        break;
                    }
                }

                for (int i = 0; i < listarray2.size(); i += 4) {
                    if (machine_id.equals(listarray2.get(i + 2))) {
                        fmachine = listarray2.get(i);
                        break;
                    }
                }

                for (int i = 0; i < listarray3.size(); i += 6) {
                    if (code.equals(listarray3.get(i + 5))) {
                        junctionnum = listarray3.get(i);
                        break;
                    }
                }

                if (fmachine != null) {

                    BigDecimal voltage1 = new BigDecimal(((double) Integer.valueOf(voltage.toString())) / 10);

                    if (count1 == 1) {
                        inSql1 = inSql + "('" + electricity + "','" + voltage1 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
                    } else {
                        inSql1 = inSql1 + ",('" + electricity + "','" + voltage1 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
                    }

                    count1++;

                    if (count1 >= 100) {
                        executeSQL(inSql1);
                        work = work + 1;
                        if (work == 5) {
                            work = 1;
                        }
                        count1 = 0;
                        inSql1 = "";
                    }
                }

                break;

            case 2:

                date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);

                for (int i = 0; i < listarray1.size(); i += 2) {
                    if (welder_id.equals(listarray1.get(i))) {
                        welder = listarray1.get(i + 1);
                        break;
                    }
                }

                for (int i = 0; i < listarray2.size(); i += 4) {
                    if (machine_id.equals(listarray2.get(i + 2))) {
                        fmachine = listarray2.get(i);
                        break;
                    }
                }

                for (int i = 0; i < listarray3.size(); i += 6) {
                    if (code.equals(listarray3.get(i + 5))) {
                        junctionnum = listarray3.get(i);
                        break;
                    }
                }

                if (fmachine != null) {
                    BigDecimal voltage2 = new BigDecimal(((double) Integer.valueOf(voltage.toString())) / 10);
                    if (count2 == 1) {
                        inSql2 = inSql + "('" + electricity + "','" + voltage2 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
                    } else {
                        inSql2 = inSql2 + ",('" + electricity + "','" + voltage2 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
                    }

                    count2++;
                    if (count2 >= 100) {
                        executeSQL(inSql2);
                        work = work + 1;
                        if (work == 5) {
                            work = 1;
                        }
                        count2 = 0;
                        inSql2 = "";
                    }
                }
                break;
            case 3:

                date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);

                for (int i = 0; i < listarray1.size(); i += 2) {
                    if (welder_id.equals(listarray1.get(i))) {
                        welder = listarray1.get(i + 1);
                        break;
                    }
                }

                for (int i = 0; i < listarray2.size(); i += 4) {
                    if (machine_id.equals(listarray2.get(i + 2))) {
                        fmachine = listarray2.get(i);
                        break;
                    }
                }

                for (int i = 0; i < listarray3.size(); i += 6) {
                    if (code.equals(listarray3.get(i + 5))) {
                        junctionnum = listarray3.get(i);
                        break;
                    }
                }

                if (fmachine != null) {

                    BigDecimal voltage3 = new BigDecimal(((double) Integer.valueOf(voltage.toString())) / 10);

                    if (count3 == 1) {
                        inSql3 = inSql + "('" + electricity + "','" + voltage3 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
                    } else {
                        inSql3 = inSql3 + ",('" + electricity + "','" + voltage3 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
                    }

                    count3++;

                    if (count3 >= 100) {
                        executeSQL(inSql3);
                        work = work + 1;
                        if (work == 5) {
                            work = 1;
                        }
                        count3 = 0;
                        inSql3 = "";
                    }
                }
                break;
            case 4:

                date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);

                for (int i = 0; i < listarray1.size(); i += 2) {
                    if (welder_id.equals(listarray1.get(i))) {
                        welder = listarray1.get(i + 1);
                        break;
                    }
                }

                for (int i = 0; i < listarray2.size(); i += 4) {
                    if (machine_id.equals(listarray2.get(i + 2))) {
                        fmachine = listarray2.get(i);
                        break;
                    }
                }

                for (int i = 0; i < listarray3.size(); i += 6) {
                    if (code.equals(listarray3.get(i + 5))) {
                        junctionnum = listarray3.get(i);
                        break;
                    }
                }

                if (fmachine != null) {

                    BigDecimal voltage4 = new BigDecimal(((double) Integer.valueOf(voltage.toString())) / 10);

                    if (count4 == 1) {
                        inSql4 = inSql + "('" + electricity + "','" + voltage4 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
                    } else {
                        inSql4 = inSql4 + ",('" + electricity + "','" + voltage4 + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + status + "','" + fitemid + "','" + goodsC_date + "','" + timesql + "','" + fmachine + "')";
                    }

                    count4++;

                    if (count4 >= 100) {
                        executeSQL(inSql4);
                        work = work + 1;
                        if (work == 5) {
                            work = 1;
                        }
                        count4 = 0;
                        inSql4 = "";
                    }
                }
                break;
        }
    }

}