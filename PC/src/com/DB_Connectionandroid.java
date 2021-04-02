package com;


import java.math.BigDecimal;
import java.sql.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class DB_Connectionandroid {

    public String select;
    public String datasend = "";
    public String fmachine;
    public String connet;
    public ArrayList<String> listarray1 = new ArrayList<String>();
    public ArrayList<String> listarray2 = new ArrayList<String>();
    public int work = 1;
    public int count1 = 1;
    public int count2 = 1;
    public int count3 = 1;
    public int count4 = 1;
    public String inSql1 = "";
    public String inSql2 = "";
    public String inSql3 = "";
    public String inSql4 = "";
    public String inSql11 = "";
    public String inSql22 = "";
    public String inSql33 = "";
    public String inSql44 = "";
    public final String inSqldata = "insert into tb_data(electricity,voltage,sensor_Num,gather_id,machine_id,welder_id,code,status,itemid,weldtime,Dtime) values";
    public final String inSqllive = "insert into tb_live_data(felectricity,fvoltage,frateofflow,fgather_no,fmachine_id,fwelder_id,fjunction_id,fstatus,fitemid,FWeldTime,FUploadDateTime) values";

    public String getId() {
        return datasend;
    }

    public void setId(String datasend) {
        this.datasend = datasend;
    }

    public static Server server;

    public java.sql.Connection conn = null;
    public java.sql.Statement stmt = null;
    public String starttime = "";
    public int length;
    private String endtime;


    public DB_Connectionandroid() {
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
                    connection = server.dbConnection.getConnection();
                    statement = connection.createStatement();
                    synchronized (sql) {
                        statement.executeUpdate(sql);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    //释放连接，归还资源
                    server.dbConnection.close(connection, statement, null);
                }
            }
        }
    }

    public void DB_androidinit1(String str) {
        length = str.length() / 28;
        Date date = new Date();
        starttime = DateTools.format("yyyy-MM-dd HH:mm:ss", date);
        try {
            if (stmt == null || stmt.isClosed() == true || !conn.isValid(1)) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(connet);
                stmt = conn.createStatement();
            }
            String sqlstart = "insert into tb_androidtask(numbers,starttime) values ('" + length + "','" + starttime + "')";
            stmt.executeUpdate(sqlstart);
        } catch (Exception e) {
            return;
        }
    }

    public void DB_androidinit2() {
        Date date = new Date();
        endtime = DateTools.format("yyyy-MM-dd HH:mm:ss", date);
        try {

            if (stmt == null || stmt.isClosed() == true || !conn.isValid(1)) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(connet);
                stmt = conn.createStatement();
            }
            String sqlend = "update tb_androidtask set endtime = '" + endtime + "' where starttime = '" + starttime + "'";
            stmt.executeUpdate(sqlend);

            length = 0;
            starttime = "";
            endtime = "";

        } catch (Exception e) {
            return;
        }
    }

    public void DB_androidupdate() {
        try {

            //得到需要处理的Android数据是起始时间
            if (stmt == null || stmt.isClosed() == true || !conn.isValid(1)) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(connet);
                stmt = conn.createStatement();
            }
            String sqlinit = "(SELECT weldtime from tb_data ORDER BY weldtime ASC LIMIT 0,1) UNION (SELECT weldtime FROM tb_data ORDER BY weldtime DESC LIMIT 0,1)";
            ResultSet rs = stmt.executeQuery(sqlinit);

            int i = 0;
            String weldtimestart = null;
            String weldtimeend = null;

            while (rs.next()) {
                if (i == 0) {
                    weldtimestart = rs.getString("weldtime");
                    i++;
                } else {
                    weldtimeend = rs.getString("weldtime");
                }
            }

            String[] timebufsta = weldtimestart.split(":");
            String[] timebufend1 = weldtimeend.split(":");
            String[] timebufend2 = timebufend1[0].split(" ");
            weldtimestart = timebufsta[0] + ":00:00";
            weldtimeend = timebufend2[0] + " " + Integer.toString(Integer.valueOf(timebufend2[1]) + 1) + ":00:00";

            long t1 = DateTools.parse("yyyy-MM-dd HH:mm:ss", weldtimestart).getTime();
            long t2 = DateTools.parse("yyyy-MM-dd HH:mm:ss", weldtimeend).getTime();

            for (long j = t1; j <= t2; j += 3600000) {

                Date d1 = new Date(j);
                String datetime1 = DateTools.format("yyyy-MM-dd HH:mm:ss", d1);
                Date d2 = new Date(j + 3600000);
                String datetime2 = DateTools.format("yyyy-MM-dd HH:mm:ss", d2);

                String sqlupdate1 = "INSERT INTO tb_standby(tb_standby.fwelder_id,tb_standby.fgather_no,tb_standby.fmachine_id,tb_standby.fjunction_id,tb_standby.fitemid,tb_standby.felectricity,"
                        + "tb_standby.fvoltage,tb_standby.frateofflow,tb_standby.fstandbytime,tb_standby.fstarttime,tb_standby.fendtime) "
                        + "SELECT tb_data.welder_id,tb_data.gather_id,tb_data.machine_id,tb_data.code,tb_data.itemid,AVG(tb_data.electricity),AVG(tb_data.voltage),AVG(tb_data.sensor_Num),"
                        + "COUNT(tb_data.fid),'" + datetime1 + "','" + datetime2 + "' FROM tb_data WHERE tb_data.status = '0' AND tb_data.weldtime BETWEEN '" + datetime1 + "' AND '" + datetime2 + "' "
                        + "GROUP BY tb_data.welder_id,tb_data.gather_id,tb_data.code";

                String sqlupdate2 = "INSERT INTO tb_work(tb_work.fwelder_id,tb_work.fgather_no,tb_work.fmachine_id,tb_work.fjunction_id,tb_work.fitemid,"
                        + "tb_work.felectricity,tb_work.fvoltage,tb_work.frateofflow,tb_work.fworktime,tb_work.fstarttime,tb_work.fendtime) "
                        + "SELECT tb_data.welder_id,tb_data.gather_id,tb_data.machine_id,tb_data.code,tb_data.itemid,AVG(tb_data.electricity),AVG(tb_data.voltage),AVG(tb_data.sensor_Num),"
                        + "COUNT(tb_data.fid),'" + datetime1 + "','" + datetime2 + "' FROM tb_data WHERE tb_data.status = '3' AND tb_data.weldtime BETWEEN '" + datetime1 + "' AND '" + datetime2 + "' "
                        + "GROUP BY tb_data.welder_id,tb_data.gather_id,tb_data.code";

                String sqlupdate3 = "INSERT INTO tb_alarm(tb_alarm.fwelder_id,tb_alarm.fgather_no,tb_alarm.fmachine_id,tb_alarm.fjunction_id,tb_alarm.fitemid,"
                        + "tb_alarm.felectricity,tb_alarm.fvoltage,tb_alarm.frateofflow,tb_alarm.falarmtime,tb_alarm.fstarttime,tb_alarm.fendtime) "
                        + "SELECT tb_data.welder_id,tb_data.gather_id,tb_data.machine_id,tb_data.code,tb_data.itemid,AVG(tb_data.electricity),AVG(tb_data.voltage),AVG(tb_data.sensor_Num),"
                        + "COUNT(tb_data.fid),'" + datetime1 + "','" + datetime2 + "' FROM tb_data LEFT JOIN tb_welded_junction ON tb_data.code = tb_welded_junction.fwelded_junction_no "
                        + "WHERE status= '3' and (tb_data.voltage > tb_welded_junction.fmax_valtage OR tb_data.electricity > tb_welded_junction.fmax_electricity "
                        + "OR tb_data.voltage < tb_welded_junction.fmin_valtage OR tb_data.electricity < tb_welded_junction.fmin_electricity) AND tb_data.weldtime BETWEEN '" + datetime1 + "' AND '" + datetime2 + "' "
                        + "GROUP BY tb_data.welder_id,tb_data.gather_id,tb_data.code";

                stmt.executeUpdate(sqlupdate1);
                stmt.executeUpdate(sqlupdate2);
                stmt.executeUpdate(sqlupdate3);

            }

            String sqldel = "TRUNCATE TABLE tb_data";
            stmt.executeUpdate(sqldel);
	        
	        /*String sqlstandby = "INSERT INTO tb_standby(tb_standby.fwelder_id,tb_standby.fgather_no,tb_standby.fmachine_id,tb_standby.fjunction_id,"
            		+ "tb_standby.fitemid,tb_standby.felectricity,tb_standby.fvoltage,tb_standby.frateofflow,tb_standby.fstandbytime,tb_standby.fstarttime,tb_standby.fendtime) SELECT "
            		+ "tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fitemid,"
            		+ "AVG(tb_live_data.felectricity),AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),'" + t1 + "','" + t2 + "' FROM tb_live_data "
            		+ "WHERE tb_live_data.fstatus = '0' AND tb_live_data.FWeldTime BETWEEN '" + t1 + "' AND '" + t2 + "' "
            		+ "GROUP BY tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fjunction_id";
            		
			  String sqlwork = "INSERT INTO tb_work(tb_work.fwelder_id,tb_work.fgather_no,tb_work.fmachine_id,tb_work.fjunction_id,tb_work.fitemid,"
                		+ "tb_work.felectricity,tb_work.fvoltage,tb_work.frateofflow,tb_work.fworktime,tb_work.fstarttime,tb_work.fendtime) SELECT tb_live_data.fwelder_id,"
                		+ "tb_live_data.fgather_no,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fitemid,AVG(tb_live_data.felectricity),"
                		+ "AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),'" + t1 + "','" + t2 + "' FROM tb_live_data "
                		+ "WHERE tb_live_data.fstatus = '3' AND tb_live_data.FWeldTime BETWEEN '" + t1 + "' AND '" + t2 + "' "
                		+ "GROUP BY tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fjunction_id";
                
              String sqlalarm = "INSERT INTO tb_alarm(tb_alarm.fwelder_id,tb_alarm.fgather_no,tb_alarm.fmachine_id,tb_alarm.fjunction_id,tb_alarm.fitemid,"
                		+ "tb_alarm.felectricity,tb_alarm.fvoltage,tb_alarm.frateofflow,tb_alarm.falarmtime,tb_alarm.fstarttime,tb_alarm.fendtime) SELECT tb_live_data.fwelder_id,"
                		+ "tb_live_data.fgather_no,tb_live_data.fmachine_id,tb_live_data.fjunction_id,tb_live_data.fitemid,AVG(tb_live_data.felectricity),"
                		+ "AVG(tb_live_data.fvoltage),AVG(tb_live_data.frateofflow),COUNT(tb_live_data.fid),'" + t1 + "','" + t2 + "' FROM tb_live_data "
                		+ "LEFT JOIN tb_welded_junction ON tb_live_data.fjunction_id = tb_welded_junction.fwelded_junction_no "
                		+ "WHERE fstatus= '3' and (tb_live_data.fvoltage > tb_welded_junction.fmax_valtage OR tb_live_data.felectricity > tb_welded_junction.fmax_electricity "
                		+ "OR tb_live_data.fvoltage < tb_welded_junction.fmin_valtage OR tb_live_data.felectricity < tb_welded_junction.fmin_electricity)"
                		+ " AND tb_live_data.FWeldTime BETWEEN '" + t1 + "' AND '" + t2 + "' "
                		+ "GROUP BY tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fjunction_id";
            		*/

        } catch (ClassNotFoundException e) {
            //System.out.println("Broken driver");
            //e.printStackTrace();
            return;
        } catch (SQLException e) {
            //System.out.println("Broken conn");
            //e.printStackTrace();
            return;
        } catch (ParseException e) {
            //System.out.println("Broken change");
            //e.printStackTrace();
        }
    }

    public void DB_Connectionandroidrun(BigDecimal electricity, BigDecimal voltage, String sensor_Num, String gather_id, String welder_id, String code, Timestamp timesql, int status, ArrayList<String> listarray2) {

        Date date;
        String nowTime;
        Timestamp goodsC_date;
        //inSql = "insert into tb_data(electricity,voltage,sensor_Num,machine_id,welder_id,code,year,month,day,hour,minute,second,status,Dtime) values('"+ electricity +"','" + voltage + "','" + sensor_Num + "','" + machine_id + "','" + welder_id + "','" + code + "','" + year + "','" + month + "','" + day + "','" + hour + "','" + minute + "','" + second + "','" + status + "','" + goodsC_date + "')";
        switch (work) {
            case 1:
                date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);
                String machine_id1 = "";
                String itemid1 = "";
                for (int i = 0; i < listarray2.size(); i += 4) {
                    if (gather_id.equals(listarray2.get(i + 2))) {
                        machine_id1 = listarray2.get(i);
                        break;
                    }
                }
                for (int i = 0; i < listarray2.size(); i += 4) {
                    if (gather_id.equals(listarray2.get(i + 2))) {
                        itemid1 = listarray2.get(i + 3);
                        break;
                    }
                }
                if (itemid1.equals("")) {
                    itemid1 = "0";
                }
                if (machine_id1.equals("")) {
                    machine_id1 = "0";
                }
                BigDecimal voltage1 = new BigDecimal(((double) Integer.valueOf(voltage.toString())) / 10);

                if (count1 == 1) {
                    inSql1 = inSqldata + "('" + electricity + "','" + voltage1 + "','" + sensor_Num + "','" + gather_id + "','" + machine_id1 + "','" + welder_id + "','" + code + "','" + status + "','" + itemid1 + "','" + timesql + "','" + goodsC_date + "')";
                    inSql11 = inSqllive + "('" + electricity + "','" + voltage1 + "','" + sensor_Num + "','" + gather_id + "','" + machine_id1 + "','" + welder_id + "','" + code + "','" + status + "','" + itemid1 + "','" + timesql + "','" + goodsC_date + "')";
                } else {
                    inSql1 = inSql1 + ",('" + electricity + "','" + voltage1 + "','" + sensor_Num + "','" + gather_id + "','" + machine_id1 + "','" + welder_id + "','" + code + "','" + status + "','" + itemid1 + "','" + timesql + "','" + goodsC_date + "')";
                    inSql11 = inSql11 + ",('" + electricity + "','" + voltage1 + "','" + sensor_Num + "','" + gather_id + "','" + machine_id1 + "','" + welder_id + "','" + code + "','" + status + "','" + itemid1 + "','" + timesql + "','" + goodsC_date + "')";
                }

                count1++;

                if (count1 >= 100) {
                    executeSQL(inSql1);
                    executeSQL(inSql11);
                    work = work + 1;
                    if (work == 5) {
                        work = 1;
                    }
                    count1 = 0;
                    inSql1 = "";
                    inSql11 = "";
                }
                break;

            case 2:

                date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);

                String machine_id2 = "";
                String itemid2 = "";
                for (int i = 0; i < listarray2.size(); i += 4) {
                    if (gather_id.equals(listarray2.get(i + 2))) {
                        machine_id2 = listarray2.get(i);
                        break;
                    }
                }
                for (int i = 0; i < listarray2.size(); i += 4) {
                    if (gather_id.equals(listarray2.get(i + 2))) {
                        itemid2 = listarray2.get(i + 3);
                        break;
                    }
                }
                if (itemid2.equals("")) {
                    itemid2 = "0";
                }
                if (machine_id2.equals("")) {
                    machine_id2 = "0";
                }
                BigDecimal voltage2 = new BigDecimal(((double) Integer.valueOf(voltage.toString())) / 10);

                if (count2 == 1) {
                    inSql2 = inSqldata + "('" + electricity + "','" + voltage2 + "','" + sensor_Num + "','" + gather_id + "','" + machine_id2 + "','" + welder_id + "','" + code + "','" + status + "','" + itemid2 + "','" + timesql + "','" + goodsC_date + "')";
                    inSql22 = inSqllive + "('" + electricity + "','" + voltage2 + "','" + sensor_Num + "','" + gather_id + "','" + machine_id2 + "','" + welder_id + "','" + code + "','" + status + "','" + itemid2 + "','" + timesql + "','" + goodsC_date + "')";
                } else {
                    inSql2 = inSql2 + ",('" + electricity + "','" + voltage2 + "','" + sensor_Num + "','" + gather_id + "','" + machine_id2 + "','" + welder_id + "','" + code + "','" + status + "','" + itemid2 + "','" + timesql + "','" + goodsC_date + "')";
                    inSql22 = inSql22 + ",('" + electricity + "','" + voltage2 + "','" + sensor_Num + "','" + gather_id + "','" + machine_id2 + "','" + welder_id + "','" + code + "','" + status + "','" + itemid2 + "','" + timesql + "','" + goodsC_date + "')";
                }

                count2++;

                if (count2 >= 100) {
                    executeSQL(inSql2);
                    executeSQL(inSql22);
                    work = work + 1;
                    if (work == 5) {
                        work = 1;
                    }
                    count2 = 0;
                    inSql2 = "";
                    inSql22 = "";
                }
                break;

            case 3:

                date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);

                String machine_id3 = "";
                String itemid3 = "";
                for (int i = 0; i < listarray2.size(); i += 4) {
                    if (gather_id.equals(listarray2.get(i + 2))) {
                        machine_id3 = listarray2.get(i);
                        break;
                    }
                }
                for (int i = 0; i < listarray2.size(); i += 4) {
                    if (gather_id.equals(listarray2.get(i + 2))) {
                        itemid3 = listarray2.get(i + 3);
                        break;
                    }
                }
                if (itemid3.equals("")) {
                    itemid3 = "0";
                }
                if (machine_id3.equals("")) {
                    machine_id3 = "0";
                }
                BigDecimal voltage3 = new BigDecimal(((double) Integer.valueOf(voltage.toString())) / 10);

                if (count3 == 1) {
                    inSql3 = inSqldata + "('" + electricity + "','" + voltage3 + "','" + sensor_Num + "','" + gather_id + "','" + machine_id3 + "','" + welder_id + "','" + code + "','" + status + "','" + itemid3 + "','" + timesql + "','" + goodsC_date + "')";
                    inSql33 = inSqllive + "('" + electricity + "','" + voltage3 + "','" + sensor_Num + "','" + gather_id + "','" + machine_id3 + "','" + welder_id + "','" + code + "','" + status + "','" + itemid3 + "','" + timesql + "','" + goodsC_date + "')";
                } else {
                    inSql3 = inSql3 + ",('" + electricity + "','" + voltage3 + "','" + sensor_Num + "','" + gather_id + "','" + machine_id3 + "','" + welder_id + "','" + code + "','" + status + "','" + itemid3 + "','" + timesql + "','" + goodsC_date + "')";
                    inSql33 = inSql33 + ",('" + electricity + "','" + voltage3 + "','" + sensor_Num + "','" + gather_id + "','" + machine_id3 + "','" + welder_id + "','" + code + "','" + status + "','" + itemid3 + "','" + timesql + "','" + goodsC_date + "')";
                }

                count3++;

                if (count3 >= 100) {
                    executeSQL(inSql3);
                    executeSQL(inSql33);
                    work = work + 1;
                    if (work == 5) {
                        work = 1;
                    }
                    count3 = 0;
                    inSql3 = "";
                    inSql33 = "";
                }
                break;

            case 4:

                date = new Date();
                nowTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
                goodsC_date = Timestamp.valueOf(nowTime);

                String machine_id4 = "";
                String itemid4 = "";
                for (int i = 0; i < listarray2.size(); i += 4) {
                    if (gather_id.equals(listarray2.get(i + 2))) {
                        machine_id4 = listarray2.get(i);
                        break;
                    }
                }
                for (int i = 0; i < listarray2.size(); i += 4) {
                    if (gather_id.equals(listarray2.get(i + 2))) {
                        itemid4 = listarray2.get(i + 3);
                        break;
                    }
                }
                if (itemid4.equals("")) {
                    itemid4 = "0";
                }
                if (machine_id4.equals("")) {
                    machine_id4 = "0";
                }
                BigDecimal voltage4 = new BigDecimal(((double) Integer.valueOf(voltage.toString())) / 10);

                if (count4 == 1) {
                    inSql4 = inSqldata + "('" + electricity + "','" + voltage4 + "','" + sensor_Num + "','" + gather_id + "','" + machine_id4 + "','" + welder_id + "','" + code + "','" + status + "','" + itemid4 + "','" + timesql + "','" + goodsC_date + "')";
                    inSql44 = inSqllive + "('" + electricity + "','" + voltage4 + "','" + sensor_Num + "','" + gather_id + "','" + machine_id4 + "','" + welder_id + "','" + code + "','" + status + "','" + itemid4 + "','" + timesql + "','" + goodsC_date + "')";
                } else {
                    inSql4 = inSql4 + ",('" + electricity + "','" + voltage4 + "','" + sensor_Num + "','" + gather_id + "','" + machine_id4 + "','" + welder_id + "','" + code + "','" + status + "','" + itemid4 + "','" + timesql + "','" + goodsC_date + "')";
                    inSql44 = inSql44 + ",('" + electricity + "','" + voltage4 + "','" + sensor_Num + "','" + gather_id + "','" + machine_id4 + "','" + welder_id + "','" + code + "','" + status + "','" + itemid4 + "','" + timesql + "','" + goodsC_date + "')";
                }

                count4++;

                if (count4 >= 100) {
                    executeSQL(inSql4);
                    executeSQL(inSql44);
                    work = work + 1;
                    if (work == 5) {
                        work = 1;
                    }
                    count4 = 0;
                    inSql4 = "";
                    inSql44 = "";
                }
                break;
        }
    }

}