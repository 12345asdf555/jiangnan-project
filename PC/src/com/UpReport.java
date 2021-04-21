package com;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;

public class UpReport {
    //更新优化报表
    String timework1 = null;
    String timework2 = null;
    String time1 = null;
    public String ip = null;
    public String ip1 = null;
    public String connet1 = "jdbc:mysql://";
    public String connet2 = ":3306/";
    public String connet3 = "?user=";
    public String connet4 = "&password=";
    public String connet5 = "&useUnicode=true&autoReconnect=true&characterEncoding=UTF8";

    public void run() {
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            connection = MysqlDBConnection.getConnection();
            stmt = connection.createStatement();

            String sqlfirstwork1 = "SELECT tb_live_data.fWeldTime FROM tb_live_data ORDER BY tb_live_data.fWeldTime ASC LIMIT 0,1";
            rs = stmt.executeQuery(sqlfirstwork1);
            while (rs.next()) {
                timework1 = rs.getString("fWeldTime");
            }

            String[] values1 = ip1.split("to");

            long datetime1 = DateTools.parse("yy-MM-dd HH:mm:ss", values1[0]).getTime();
            //System.out.println(datetime1);

            String sqlfirstwork2 = "SELECT tb_live_data.fWeldTime FROM tb_live_data ORDER BY tb_live_data.fWeldTime DESC LIMIT 0,1";
            rs = stmt.executeQuery(sqlfirstwork2);
            while (rs.next()) {
                timework2 = rs.getString("fWeldTime");
            }

            long datetime2 = DateTools.parse("yy-MM-dd HH:mm:ss", values1[1]).getTime();
            //System.out.println(datetime2);

            for (long i = datetime1; i <= datetime2; i += 3600000) {

                Date d1 = new Date(i);
                String t1 = DateTools.format("yy-MM-dd HH:mm:ss", d1);
                Date d2 = new Date(i + 3600000);
                String t2 = DateTools.format("yy-MM-dd HH:mm:ss", d2);
                //System.out.println(t1);
                //System.out.println(t2);

                String sqlstandby = "INSERT INTO tb_standby(tb_standby.fwelder_id,tb_standby.fgather_no,tb_standby.fmachine_id,tb_standby.fjunction_id,"
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
                        + "WHERE fstatus= '3' and tb_welded_junction.fitemid = tb_live_data.fitemid and (tb_live_data.fvoltage > tb_welded_junction.fmax_valtage OR tb_live_data.felectricity > tb_welded_junction.fmax_electricity "
                        + "OR tb_live_data.fvoltage < tb_welded_junction.fmin_valtage OR tb_live_data.felectricity < tb_welded_junction.fmin_electricity)"
                        + " AND tb_live_data.FWeldTime BETWEEN '" + t1 + "' AND '" + t2 + "' "
                        + "GROUP BY tb_live_data.fwelder_id,tb_live_data.fgather_no,tb_live_data.fjunction_id";

                stmt.executeUpdate(sqlstandby);
                stmt.executeUpdate(sqlwork);
                stmt.executeUpdate(sqlalarm);

                Thread.sleep(50);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MysqlDBConnection.close(connection, stmt, rs);
        }
    }
}
