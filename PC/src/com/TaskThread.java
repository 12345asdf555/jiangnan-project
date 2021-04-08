package com;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 定时任务
 */
public class TaskThread extends Timer {

    /**
     * 定时任务对象
     */
    private TaskThread timer = null;
    private TaskThread timer2 = null;
    private TaskThread timer3 = null;
    private static final String connectUrl = "jdbc:mysql://10.38.3.30:3306/ciwjn?user=root&password=123&useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT";
    //周期性线程池，处理实时数据分表的创建
    private static final ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);

    //时间间隔(一天)
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy_MM_dd");

    public void start() {
        //检测本周实时数据表是否已经创建
        detectionRtdataTable();
        //下周实时数据表的创建
        shardingJdbcMysql();
        //每天凌晨0点更新实时数据表名
        updateRtdataTableName();
        //每小时更新四张数据统计表
        rtDatastatistics();
        //每小时更新焊工、焊机、任务等数据
        scheduleUpdateData();
    }

    /**
     * 获取本周实时数据表名
     *
     * @return
     */
    public static String getNowTableName() {
        String nowMonday = sdfDate.format(DateUtil.getNowMonday());
        String nowSunday = sdfDate.format(DateUtil.getNowSunday());
        //命名方式为周一到周日：rtData2021-04-05_2021-04-11
        return ("rtdata" + nowMonday + "_" + nowSunday);
    }

    /**
     * 定时任务，实时数据表名周期性更新
     */
    private void updateRtdataTableName() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0); //凌晨0点
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime(); //第一次执行定时任务的时间
        //如果第一次执行定时任务的时间 小于当前的时间
        //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
        if (date.before(new Date())) {
            date = this.addDay(date, 1);
        }
        timer = new TaskThread();
        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //执行代码
                System.out.println(sdf.format(System.currentTimeMillis()) + "定时任务：实时数据表名更新：" + getNowTableName());
                DB_Connectionmysql.inSqlbase = DB_Connectionmysql.insertrtDataSql(getNowTableName());
            }
        }, date, PERIOD_DAY);
    }

    /**
     * 实时数据统计，四张状态表更新
     */
    private void rtDatastatistics() {
        Date date = new Date();
        String nowtime = DateTools.format("HH:mm:ss", date);
        String[] timesplit = nowtime.split(":");
        String hour = timesplit[0];
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour) + 1); // 控制时
        calendar.set(Calendar.MINUTE, 0);    // 控制分
        calendar.set(Calendar.SECOND, 0);    // 控制秒
        Date time = calendar.getTime();
        timer2 = new TaskThread();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                Connection connection = null;
                Statement mysqlStmt = null;
                Connection dataConn = null;
                Statement rtDataStmt = null;
                String nowTableName = getNowTableName(); //本周的实时数据表名
                try {
                    //业务数据库
                    connection = MysqlDBConnection.getConnection();
                    mysqlStmt = connection.createStatement();
                    //实时数据库
                    dataConn = LiveDataDBConnection.getConnection();
                    rtDataStmt = dataConn.createStatement();

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
                    String sqlfirstwork = "SELECT tb_work.fUploadDataTime FROM tb_work ORDER BY tb_work.fUploadDataTime DESC LIMIT 1";
                    String sqlfirststandby = "SELECT tb_standby.fUploadDataTime FROM tb_standby ORDER BY tb_standby.fUploadDataTime DESC LIMIT 1";
                    String sqlfirstalarm = "SELECT tb_alarm.fUploadDataTime FROM tb_alarm ORDER BY tb_alarm.fUploadDataTime DESC LIMIT 1";
                    String sqlfirstwarn = "SELECT tb_warn.fUploadDataTime FROM tb_warn ORDER BY tb_warn.fUploadDataTime DESC LIMIT 1";
                    ResultSet rs1 = mysqlStmt.executeQuery(sqlfirstwork);
                    if (rs1.next()) {
                        timework = rs1.getString("fUploadDataTime");
                    }
                    ResultSet rs2 = mysqlStmt.executeQuery(sqlfirststandby);
                    if (rs2.next()) {
                        timestandby = rs2.getString("fUploadDataTime");
                    }
                    ResultSet rs3 = mysqlStmt.executeQuery(sqlfirstalarm);
                    if (rs3.next()) {
                        timealarm = rs3.getString("fUploadDataTime");
                    }
                    ResultSet rs4 = mysqlStmt.executeQuery(sqlfirstwarn);
                    if (rs4.next()) {
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

                    /**
                     * 待机数据每小时统计一次
                     */
                    String rtdata_standby_sql = "SELECT fwelder_id,fgather_no,fmachine_id,fjunction_id,fitemid,AVG(felectricity) felectricity,AVG(fvoltage) fvoltage,AVG(frateofflow) frateofflow,COUNT(fid) fstandbytime," +
                            "'" + time3 + "','" + time2 + "',fwelder_no,fjunction_no,fweld_no,fchannel,fmax_electricity,fmin_electricity,fmax_voltage,fmin_voltage,fwelder_itemid," +
                            "fjunction_itemid,fmachine_itemid,AVG(fwirefeedrate) fwirefeedrate,fmachinemodel,fwirediameter,fmaterialgas,AVG(fwmax_electricity) fwmax_electricity,AVG(fwmin_electricity) fwmin_electricity," +
                            "AVG(fwmax_voltage) fwmax_voltage,AVG(fwmin_voltage) fwmin_voltage,fstatus,fsolder_layer,fweld_bead " +
                            "FROM " + nowTableName + " " +
                            "WHERE fstatus = '0' AND FWeldTime BETWEEN '" + timestandby + "' AND '" + time2 + "' GROUP BY fwelder_id,fgather_no,fjunction_id,fstatus," +
                            "fmachine_id,fsolder_layer,fweld_bead";
                    //实时数据库中对待机数据统计查询
                    ResultSet standby_resultSet = rtDataStmt.executeQuery(rtdata_standby_sql);
                    if (standby_resultSet.next()) {
                        while (standby_resultSet.next()) {
                            ResultSetMetaData standbyMetaData = standby_resultSet.getMetaData();
                            TbStandbyModel tbStandbyModel = new TbStandbyModel();
                            for (int i = 0; i < standbyMetaData.getColumnCount(); ++i) {
                                String columnLable = standbyMetaData.getColumnLabel(i + 1);
                                Object columnValue = standby_resultSet.getObject(columnLable);
                                if (columnLable.equals(time3)){
                                    columnLable = "fstarttime";
                                }
                                if (columnLable.equals(time2)){
                                    columnLable = "fendtime";
                                }
                                if (columnValue == null || columnValue.equals("null")){
                                    columnValue = 0;
                                }
                                //实体类赋值
                                BeanUtils.setProperty(tbStandbyModel, columnLable, columnValue);
                            }
                            //待机数据插入到业务数据库
                            String tb_standby_sql = getInsertSql("tb_standby", TbStandbyModel.class, tbStandbyModel);
                            mysqlStmt.executeUpdate(tb_standby_sql);
                        }
                    }

                    /**
                     * 焊接数据每小时统计一次
                     */
                    String rtdata_work_sql = "SELECT fwelder_id,fgather_no,fmachine_id,fjunction_id,fitemid,AVG(felectricity) felectricity,AVG(fvoltage) fvoltage,AVG(frateofflow) frateofflow,COUNT(fid) fworktime," +
                            "'" + time3 + "','" + time2 + "',fwelder_no,fjunction_no,fweld_no,fchannel,fmax_electricity,fmin_electricity,fmax_voltage,fmin_voltage,fwelder_itemid," +
                            "fjunction_itemid,fmachine_itemid,AVG(fwirefeedrate) fwirefeedrate,fmachinemodel,fwirediameter,fmaterialgas,AVG(fwmax_electricity) fwmax_electricity,AVG(fwmin_electricity) fwmin_electricity," +
                            "AVG(fwmax_voltage) fwmax_voltage,AVG(fwmin_voltage) fwmin_voltage,fstatus,fsolder_layer,fweld_bead " +
                            "FROM " + nowTableName + " " +
                            "WHERE (fstatus = '3' OR fstatus= '5' OR fstatus= '7' OR fstatus= '99') AND FWeldTime BETWEEN '" + timework + "' AND '" + time2 + "' " +
                            "GROUP BY fwelder_id,fgather_no,fjunction_id,fstatus,fmachine_id,fsolder_layer,fweld_bead";
                    //实时数据库中对焊接数据统计查询
                    ResultSet work_resultSet = rtDataStmt.executeQuery(rtdata_work_sql);
                    if (work_resultSet.next()) {
                        while (work_resultSet.next()) {
                            ResultSetMetaData workMetaData = work_resultSet.getMetaData();
                            TbWorkModel tbWorkModel = new TbWorkModel();
                            for (int i = 0; i < workMetaData.getColumnCount(); ++i) {
                                String columnLable = workMetaData.getColumnLabel(i + 1);
                                Object columnValue = work_resultSet.getObject(columnLable);
                                if (columnLable.equals(time3)){
                                    columnLable = "fstarttime";
                                }
                                if (columnLable.equals(time2)){
                                    columnLable = "fendtime";
                                }
                                if (columnValue == null || columnValue.equals("null")){
                                    columnValue = 0;
                                }
                                //实体类赋值
                                BeanUtils.setProperty(tbWorkModel, columnLable, columnValue);
                            }
                            //焊接数据插入到业务数据库
                            String tb_work_sql = getInsertSql("tb_work", TbWorkModel.class, tbWorkModel);
                            int i = mysqlStmt.executeUpdate(tb_work_sql);
                            //System.out.println("焊接数据统计sql--" + i + "-->tb_work_sql:" + tb_work_sql);
                        }
                    }

                    /**
                     * 故障数据每小时统计一次
                     */
                    String rtdata_warn_sql = "SELECT fwelder_id,fgather_no,fmachine_id,fjunction_id,fitemid,AVG(felectricity) felectricity,AVG(fvoltage) fvoltage,AVG(frateofflow) frateofflow,COUNT(fid) fwarntime," +
                            "'" + time3 + "','" + time2 + "',fwelder_no,fjunction_no,fweld_no,fchannel,fmax_electricity,fmin_electricity,fmax_voltage,fmin_voltage,fwelder_itemid," +
                            "fjunction_itemid,fmachine_itemid,AVG(fwirefeedrate) fwirefeedrate,fmachinemodel,fwirediameter,fmaterialgas,AVG(fwmax_electricity) fwmax_electricity,AVG(fwmin_electricity) fwmin_electricity," +
                            "AVG(fwmax_voltage) fwmax_voltage,AVG(fwmin_voltage) fwmin_voltage,fstatus,fsolder_layer,fweld_bead " +
                            "FROM " + nowTableName + " WHERE fstatus != '0' AND fstatus != '3' AND fstatus != '5' " +
                            "AND fstatus != '7' AND FWeldTime BETWEEN '" + timewarn + "' AND '" + time2 + "' GROUP BY fwelder_id,fgather_no,fjunction_id,fstatus,fmachine_id," +
                            "fsolder_layer,fweld_bead";
                    //实时数据库中对故障数据统计查询
                    ResultSet warn_resultSet = rtDataStmt.executeQuery(rtdata_warn_sql);
                    if (warn_resultSet.next()) {
                        while (warn_resultSet.next()) {
                            ResultSetMetaData warnMetaData = warn_resultSet.getMetaData();
                            TbWarnModel tbWarnModel = new TbWarnModel();
                            for (int i = 0; i < warnMetaData.getColumnCount(); ++i) {
                                String columnLable = warnMetaData.getColumnLabel(i + 1);
                                Object columnValue = warn_resultSet.getObject(columnLable);
                                if (columnLable.equals(time3)){
                                    columnLable = "fstarttime";
                                }
                                if (columnLable.equals(time2)){
                                    columnLable = "fendtime";
                                }
                                if (columnValue == null || columnValue.equals("null")){
                                    columnValue = 0;
                                }
                                //实体类赋值
                                BeanUtils.setProperty(tbWarnModel, columnLable, columnValue);
                            }
                            //故障数据插入到业务数据库
                            String tb_warn_sql = getInsertSql("tb_warn", TbWarnModel.class, tbWarnModel);
                            mysqlStmt.executeUpdate(tb_warn_sql);
                        }
                    }

                    /**
                     * 超规范数据每小时统计一次
                     */
                    String rtdata_alarm_sql = "SELECT fwelder_id,fgather_no,fmachine_id,fjunction_id,fitemid,AVG(felectricity) felectricity,AVG(fvoltage) fvoltage,AVG(frateofflow) frateofflow,COUNT(fid) falarmtime," +
                            "'" + time3 + "','" + time2 + "',fwelder_no,fjunction_no,fweld_no,fchannel,fmax_electricity,fmin_electricity,fmax_voltage,fmin_voltage,fwelder_itemid," +
                            "fjunction_itemid,fmachine_itemid,AVG(fwirefeedrate) fwirefeedrate,fmachinemodel,fwirediameter,fmaterialgas,AVG(fwmax_electricity) fwmax_electricity,AVG(fwmin_electricity) fwmin_electricity," +
                            "AVG(fwmax_voltage) fwmax_voltage,AVG(fwmin_voltage) fwmin_voltage,fstatus,fsolder_layer,fweld_bead " +
                            "FROM " + nowTableName + " WHERE (fstatus= '98' OR fstatus= '99') " +
                            "AND FWeldTime BETWEEN '" + timealarm + "' AND '" + time2 + "' GROUP BY fwelder_id,fgather_no,fjunction_id,fstatus,fmachine_id,fsolder_layer," +
                            "fweld_bead";
                    //实时数据库中对超规范数据统计查询
                    ResultSet alarm_resultSet = rtDataStmt.executeQuery(rtdata_alarm_sql);
                    if (alarm_resultSet.next()) {
                        while (alarm_resultSet.next()) {
                            ResultSetMetaData alarmMetaData = alarm_resultSet.getMetaData();
                            TbAlarmModel tbAlarmModel = new TbAlarmModel();
                            for (int i = 0; i < alarmMetaData.getColumnCount(); ++i) {
                                String columnLable = alarmMetaData.getColumnLabel(i + 1);
                                Object columnValue = alarm_resultSet.getObject(columnLable);
                                if (columnLable.equals(time3)){
                                    columnLable = "fstarttime";
                                }
                                if (columnLable.equals(time2)){
                                    columnLable = "fendtime";
                                }
                                if (columnValue == null || columnValue.equals("null")){
                                    columnValue = 0;
                                }
                                //实体类赋值
                                BeanUtils.setProperty(tbAlarmModel, columnLable, columnValue);
                            }
                            //超规范数据插入到业务数据库
                            String tb_alarm_sql = getInsertSql("tb_alarm", TbAlarmModel.class, tbAlarmModel);
                            mysqlStmt.executeUpdate(tb_alarm_sql);
                        }
                    }
                    System.out.println(sdf.format(System.currentTimeMillis()) + "实时数据的状态表每小时更新完成：" + nowTableName);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    MysqlDBConnection.close(connection, mysqlStmt, null);
                    LiveDataDBConnection.close(dataConn, rtDataStmt, null);
                }
            }
        }, time, 1000 * 60 * 60);
    }

    /**
     * 每小时更新数据
     */
    private void scheduleUpdateData() {
        timer3 = new TaskThread();
        timer3.schedule(new TimerTask() {
            @Override
            public void run() {
                Connection conn = null;
                Statement stmt = null;
                try {
                    conn = MysqlDBConnection.getConnection();
                    stmt = conn.createStatement();
                    DB_Connectioncode check = new DB_Connectioncode(stmt, conn, connectUrl);

                    ArrayList<String> listarray1 = check.getId1();
                    ArrayList<String> listarray2 = check.getId2();
                    ArrayList<String> listarray3 = check.getId3();

                    NettyServerHandler.mysql.listarray1 = listarray1;
                    NettyServerHandler.mysql.listarray2 = listarray2;
                    NettyServerHandler.mysql.listarray3 = listarray3;
                    NettyServerHandler.android.listarray1 = listarray1;
                    NettyServerHandler.android.listarray2 = listarray2;
                    NettyServerHandler.listarray1 = listarray1;
                    NettyServerHandler.listarray2 = listarray2;
                    NettyServerHandler.listarray3 = listarray3;

                    System.out.println(sdf.format(System.currentTimeMillis()) + "->焊机、焊工、任务等数据每小时更新一次");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    MysqlDBConnection.close(conn, stmt, null);
                }
            }
        }, 0, 6000000);
    }

    /**
     * 工具类。根据实体类获取插入sql
     *
     * @param tablename
     * @param clazz
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String getInsertSql(String tablename, Class<T> clazz, T t) {
        //insert into table_name (column_name1,column_name2, ...) values (value1,value2, ...)
        String sql = "";
        Field[] fields = ReflectUtil.getFieldsDirectly(clazz, false);
        StringBuffer topHalf = new StringBuffer("insert into " + tablename + " (");
        StringBuffer afterAalf = new StringBuffer("values (");
        for (Field field : fields) {
            topHalf.append(field.getName() + ",");
            if (ReflectUtil.getFieldValue(t, field.getName()) instanceof String) {
                afterAalf.append("'" + ReflectUtil.getFieldValue(t, field.getName()) + "',");
            } else {
                afterAalf.append(ReflectUtil.getFieldValue(t, field.getName()) + ",");
            }
        }
        topHalf = new StringBuffer(StrUtil.removeSuffix(topHalf.toString(), ","));
        afterAalf = new StringBuffer(StrUtil.removeSuffix(afterAalf.toString(), ","));
        topHalf.append(") ");
        afterAalf.append(") ");
        sql = topHalf.toString() + afterAalf.toString();
        return sql;
    }

    // 增加或减少天数
    private Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

    //本周实时数据表的创建
    private void detectionRtdataTable() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                createRealDataTable(getNowTableName());
            }
        }).start();
    }

    //下周的实时数据表的创建
    private void shardingJdbcMysql() {
        /**
         * 开启先执行一次，每隔1天执行一次
         * 任务：如果是周日，判断实时数据表是否存在，不存在则创建
         */
        scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                Calendar c = Calendar.getInstance();
                c.setTime(new Date(System.currentTimeMillis()));
                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                //如果是周日，则创建下周的实时数据表
                if (dayOfWeek == 1) {
                    String nextmon = sdfDate.format(DateUtil.getNextMonday());
                    String nextsun = sdfDate.format(DateUtil.getNextSunday());
                    //命名方式为周一到周日：rtData2021-04-05_2021-04-11
                    String tableName = "rtdata" + nextmon + "_" + nextsun;
                    createRealDataTable(tableName);
                }
            }
        }, 0, 1, TimeUnit.DAYS);
    }

    //创建实时数据表【水平分表，每周一张表】
    private static void createRealDataTable(String tableName) {
        if (null != tableName && !"".equals(tableName)) {
            Connection connection = null;
            Statement statement = null;
            try {
                connection = LiveDataDBConnection.getConnection();
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("show tables like '" + tableName + "'");
                //如果不存在，则进行创建实时数据表
                if (!resultSet.next()) {
                    System.out.println("tableName:" + tableName + ":不存在，进行创建");
                    statement.executeUpdate("CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                            "fid bigint(20) NOT NULL AUTO_INCREMENT," +
                            "fwelder_id bigint(20) DEFAULT '0' COMMENT '焊工id'," +
                            "fgather_no varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT '0' COMMENT '采集编号'," +
                            "fmachine_id bigint(20) DEFAULT '0' COMMENT '焊机id'," +
                            "fjunction_id bigint(20) DEFAULT '0' COMMENT '焊口id'," +
                            "fitemid bigint DEFAULT '17'," +
                            "felectricity decimal(10,2) DEFAULT NULL," +
                            "fvoltage decimal(10,2) DEFAULT NULL," +
                            "frateofflow decimal(10,2) DEFAULT NULL," +
                            "fstatus int DEFAULT '0' COMMENT '焊机状态'," +
                            "fwelder_no varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT '0' COMMENT '焊工编号'," +
                            "fjunction_no varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '0' COMMENT '焊口编号'," +
                            "fweld_no varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT '0' COMMENT '焊机编号'," +
                            "fchannel int DEFAULT NULL COMMENT '通道'," +
                            "fmax_electricity decimal(10,2) DEFAULT NULL," +
                            "fmin_electricity decimal(10,2) DEFAULT NULL," +
                            "fmax_voltage decimal(10,2) DEFAULT NULL," +
                            "fmin_voltage decimal(10,2) DEFAULT NULL," +
                            "fwelder_itemid varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT '0' COMMENT '焊工编号'," +
                            "fjunction_itemid varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT '0' COMMENT '焊口编号'," +
                            "fmachine_itemid varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT '0' COMMENT '焊机编号'," +
                            "fwirefeedrate decimal(10,2) DEFAULT NULL COMMENT '送丝速度'," +
                            "fweldingrate decimal(10,2) DEFAULT NULL," +
                            "fmachinemodel varchar(20) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT '0' COMMENT '焊机型号'," +
                            "fwirediameter decimal(10,2) DEFAULT NULL COMMENT '焊丝直径'," +
                            "fmaterialgas int DEFAULT '0' COMMENT '材质气体'," +
                            "fwmax_electricity decimal(10,2) DEFAULT NULL," +
                            "fwmin_electricity decimal(10,2) DEFAULT NULL," +
                            "fwmax_voltage decimal(10,2) DEFAULT NULL," +
                            "fwmin_voltage decimal(10,2) DEFAULT NULL," +
                            "fweldheatinput decimal(10,2) DEFAULT NULL," +
                            "fhatwirecurrent decimal(10,2) DEFAULT NULL," +
                            "fvibrafrequency decimal(10,2) DEFAULT NULL," +
                            "FWeldTime datetime DEFAULT NULL COMMENT '焊机时间'," +
                            "FUploadDateTime datetime DEFAULT NULL COMMENT '创建时间'," +
                            "fsolder_layer int DEFAULT NULL COMMENT '焊道号'," +
                            "fweld_bead int DEFAULT NULL COMMENT '焊层号'," +
                            "PRIMARY KEY (fid) USING BTREE," +
                            "KEY FweldTime (FWeldTime) USING BTREE" +
                            ") ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=DYNAMIC;");
                } else {
                    System.out.println("tableName:" + tableName + ":存在");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                LiveDataDBConnection.close(connection, statement, null);
            }
        }
    }

}
