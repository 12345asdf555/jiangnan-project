package com;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class Mysql {

    public java.sql.Statement stmt;
    public ArrayList<String> listarray1;
    public ArrayList<String> listarray2;
    public ArrayList<String> listarray3;
    public DB_Connectionmysql db;

    public Mysql() {
        db = new DB_Connectionmysql();
        this.db = db;
    }

    public void Mysqlbase(String str) {
        Date time;
        Timestamp timesql = null;
        if (str.length() == 284) {

            //校验第一位是否为FA末位是否为F5
            String check1 = str.substring(0, 2);
            String check11 = str.substring(282, 284);
            if (check1.equals("7E") && check11.equals("7D")) {

                long welderid = Integer.valueOf(str.substring(34, 38));
                long weldid = Integer.valueOf(str.substring(18, 22));
                long gatherno = Integer.valueOf(str.substring(14, 18),16);//采集编号
                long itemid = Integer.valueOf(str.substring(232, 234));
                String weldmodel = Integer.valueOf(str.subSequence(12, 14).toString(), 16).toString();

                for (int a = 0; a < 161; a += 80) {
                    long junctionid = Integer.valueOf(str.substring(70 + a, 78 + a));
                    BigDecimal electricity = new BigDecimal(Integer.valueOf(str.subSequence(50 + a, 54 + a).toString(), 16));
                    BigDecimal voltage = new BigDecimal(Integer.valueOf(str.subSequence(54 + a, 58 + a).toString(), 16));
                    int status = Integer.parseInt(str.subSequence(78 + a, 80 + a).toString(), 16);
                    BigDecimal fwirefeedrate = new BigDecimal(Integer.valueOf(str.subSequence(58 + a, 62 + a).toString(), 16));
                    String year = Integer.valueOf(str.subSequence(38 + a, 40 + a).toString(), 16).toString();
                    String month = Integer.valueOf(str.subSequence(40 + a, 42 + a).toString(), 16).toString();
                    String day = Integer.valueOf(str.subSequence(42 + a, 44 + a).toString(), 16).toString();
                    String hour = Integer.valueOf(str.subSequence(44 + a, 46 + a).toString(), 16).toString();
                    String minute = Integer.valueOf(str.subSequence(46 + a, 48 + a).toString(), 16).toString();
                    String second = Integer.valueOf(str.subSequence(48 + a, 50 + a).toString(), 16).toString();
                    String strdate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;

                    try {
                        time = DateTools.parse("yy-MM-dd HH:mm:ss", strdate);
                        timesql = new Timestamp(time.getTime());
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        //e.printStackTrace();
                    }

                    int channel = Integer.valueOf(str.subSequence(100 + a, 102 + a).toString(), 16);

                    //焊接电流电压上下限
                    BigDecimal maxelectricity = new BigDecimal((Integer.valueOf(str.subSequence(84 + a, 88 + a).toString(), 16).intValue()) + (Integer.valueOf(str.subSequence(92 + a, 94 + a).toString(), 16).intValue()));
                    BigDecimal minelectricity = null;
                    if ((Integer.valueOf(str.subSequence(84 + a, 88 + a).toString(), 16).intValue() - Integer.valueOf(str.subSequence(92 + a, 94 + a).toString(), 16)) < 0) {
                        minelectricity = new BigDecimal(0);
                    } else {
                        minelectricity = new BigDecimal((Integer.valueOf(str.subSequence(84 + a, 88 + a).toString(), 16).intValue()) - (Integer.valueOf(str.subSequence(92 + a, 94 + a).toString(), 16).intValue()));
                    }
                    //BigDecimal minelectricity = new BigDecimal((Integer.valueOf(str.subSequence(84+a, 88+a).toString(),16).intValue())-(Integer.valueOf(str.subSequence(92+a, 94+a).toString(),16).intValue()));
                    BigDecimal maxvoltage = new BigDecimal((Integer.valueOf(str.subSequence(88 + a, 92 + a).toString(), 16).intValue()) + (Integer.valueOf(str.subSequence(94 + a, 96 + a).toString(), 16).intValue()));
                    BigDecimal minvoltage = null;
                    if ((Integer.valueOf(str.subSequence(88 + a, 92 + a).toString(), 16).intValue() - Integer.valueOf(str.subSequence(94 + a, 96 + a).toString(), 16)) < 0) {
                        minvoltage = new BigDecimal(0);
                    } else {
                        minvoltage = new BigDecimal((Integer.valueOf(str.subSequence(88 + a, 92 + a).toString(), 16).intValue()) - (Integer.valueOf(str.subSequence(94 + a, 96 + a).toString(), 16).intValue()));
                    }
                    //BigDecimal minvoltage = new BigDecimal((Integer.valueOf(str.subSequence(88+a, 92+a).toString(),16).intValue())-(Integer.valueOf(str.subSequence(94+a, 96+a).toString(),16).intValue()));

                    //报警电流电压上下限wmaxvoltage
                    BigDecimal wmaxelectricity = new BigDecimal(Integer.valueOf(str.subSequence(102 + a, 106 + a).toString(), 16).intValue());
                    BigDecimal wmaxvoltage = new BigDecimal(Integer.valueOf(str.subSequence(106 + a, 110 + a).toString(), 16).intValue());
                    BigDecimal wminelectricity = new BigDecimal(Integer.valueOf(str.subSequence(110 + a, 114 + a).toString(), 16).intValue());
                    BigDecimal wminvoltage = new BigDecimal(Integer.valueOf(str.subSequence(114 + a, 118 + a).toString(), 16).intValue());

                    BigDecimal fwirediameter = new BigDecimal(Integer.valueOf(str.subSequence(80 + a, 82 + a).toString(), 16));
                    int fmaterialgas = Integer.parseInt(str.subSequence(82 + a, 84 + a).toString(), 16);

                    db.DB_Connectionmysqlrun(welderid, weldid, gatherno, itemid, weldid, weldmodel, junctionid, electricity, voltage, status, fwirefeedrate, timesql, channel, maxelectricity, minelectricity, maxvoltage, minvoltage, fwirediameter, fmaterialgas, listarray1, listarray2, listarray3, wmaxelectricity, wminelectricity, wmaxvoltage, wminvoltage);

                }
            }
        } else if (str.length() == 124) {      //松下
            //校验第一位是否为FA末位是否为F5
            String check1 = str.substring(0, 2);
            String check11 = str.substring(122, 124);
            if (check1.equals("7E") && check11.equals("7D")) {

                long welderid = Integer.valueOf(str.substring(34, 38));
                long weldid = Integer.valueOf(str.substring(18, 22));
                //long gatherid = Integer.valueOf(str.substring(14, 18));
                long gatherno = Integer.valueOf(str.substring(14, 18),16);//采集编号
                long itemid = Integer.valueOf(str.substring(120, 122));
                String weldmodel = Integer.valueOf(str.subSequence(12, 14).toString(), 16).toString();

                long junctionid = Integer.valueOf(str.substring(70, 78));
                BigDecimal electricity = new BigDecimal(Integer.valueOf(str.subSequence(50, 54).toString(), 16));
                BigDecimal voltage = new BigDecimal(Integer.valueOf(str.subSequence(54, 58).toString(), 16));
                int status = Integer.parseInt(str.subSequence(78, 80).toString(), 16);
                BigDecimal fwirefeedrate = new BigDecimal(Integer.valueOf(str.subSequence(58, 62).toString(), 16));
                String year = Integer.valueOf(str.subSequence(38, 40).toString(), 16).toString();
                String month = Integer.valueOf(str.subSequence(40, 42).toString(), 16).toString();
                String day = Integer.valueOf(str.subSequence(42, 44).toString(), 16).toString();
                String hour = Integer.valueOf(str.subSequence(44, 46).toString(), 16).toString();
                String minute = Integer.valueOf(str.subSequence(46, 48).toString(), 16).toString();
                String second = Integer.valueOf(str.subSequence(48, 50).toString(), 16).toString();
                String strdate = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;

                try {
                    time = DateTools.parse("yy-MM-dd HH:mm:ss", strdate);
                    timesql = new Timestamp(time.getTime());
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    //e.printStackTrace();
                }

                int channel = Integer.valueOf(str.subSequence(100, 102).toString(), 16);
				/*BigDecimal maxelectricity = new BigDecimal(Integer.valueOf(str.subSequence(84, 88).toString(),16));
				BigDecimal minelectricity = new BigDecimal(Integer.valueOf(str.subSequence(88, 92).toString(),16));
				BigDecimal maxvoltage = new BigDecimal(Integer.valueOf(str.subSequence(92, 96).toString(),16));
				BigDecimal minvoltage = new BigDecimal(Integer.valueOf(str.subSequence(96, 100).toString(),16));*/
                BigDecimal fwirediameter = new BigDecimal(Integer.valueOf(str.subSequence(80, 82).toString(), 16));

                //焊接电流电压上下限
                BigDecimal maxelectricity = new BigDecimal((Integer.valueOf(str.subSequence(84, 88).toString(), 16).intValue()) + (Integer.valueOf(str.subSequence(92, 94).toString(), 16).intValue()));
                BigDecimal minelectricity = null;
                if ((Integer.valueOf(str.subSequence(84, 88).toString(), 16).intValue() - Integer.valueOf(str.subSequence(92, 94).toString(), 16)) < 0) {
                    minelectricity = new BigDecimal(0);
                } else {
                    minelectricity = new BigDecimal((Integer.valueOf(str.subSequence(84, 88).toString(), 16).intValue()) - (Integer.valueOf(str.subSequence(92, 94).toString(), 16).intValue()));
                }
                //BigDecimal minelectricity = new BigDecimal((Integer.valueOf(str.subSequence(84+a, 88+a).toString(),16).intValue())-(Integer.valueOf(str.subSequence(92+a, 94+a).toString(),16).intValue()));
                BigDecimal maxvoltage = new BigDecimal((Integer.valueOf(str.subSequence(88, 92).toString(), 16).intValue()) + (Integer.valueOf(str.subSequence(94, 96).toString(), 16).intValue()));
                BigDecimal minvoltage = null;
                if ((Integer.valueOf(str.subSequence(88, 92).toString(), 16).intValue() - Integer.valueOf(str.subSequence(94, 96).toString(), 16)) < 0) {
                    minvoltage = new BigDecimal(0);
                } else {
                    minvoltage = new BigDecimal((Integer.valueOf(str.subSequence(88, 92).toString(), 16).intValue()) - (Integer.valueOf(str.subSequence(94, 96).toString(), 16).intValue()));
                }
                //BigDecimal minvoltage = new BigDecimal((Integer.valueOf(str.subSequence(88+a, 92+a).toString(),16).intValue())-(Integer.valueOf(str.subSequence(94+a, 96+a).toString(),16).intValue()));

                //报警电流电压上下限wmaxvoltage
                BigDecimal wmaxelectricity = new BigDecimal(Integer.valueOf(str.subSequence(102, 106).toString(), 16).intValue());
                BigDecimal wmaxvoltage = new BigDecimal(Integer.valueOf(str.subSequence(106, 110).toString(), 16).intValue());
                BigDecimal wminelectricity = new BigDecimal(Integer.valueOf(str.subSequence(110, 114).toString(), 16).intValue());
                BigDecimal wminvoltage = new BigDecimal(Integer.valueOf(str.subSequence(114, 118).toString(), 16).intValue());
                //BigDecimal wminvoltage = new BigDecimal((Integer.valueOf(str.subSequence(88+a, 92+a).toString(),16).intValue())-(Integer.valueOf(str.subSequence(96+a, 98+a).toString(),16).intValue()));

                int fmaterialgas = Integer.parseInt(str.subSequence(82, 84).toString(), 16);

                db.DB_Connectionmysqlrun(welderid, weldid, gatherno, itemid, weldid, weldmodel, junctionid, electricity, voltage, status, fwirefeedrate, timesql, channel, maxelectricity, minelectricity, maxvoltage, minvoltage, fwirediameter, fmaterialgas, listarray1, listarray2, listarray3, wmaxelectricity, wminelectricity, wmaxvoltage, wminvoltage);
            }
        } else {
        }
    }

    public void Mysqlrun(String str) {
        try {

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

                            for (int i = 0; i < 78; i += 26) {

                                BigDecimal electricity = new BigDecimal(Integer.valueOf(str.subSequence(26 + i, 30 + i).toString(), 16));
                                BigDecimal voltage = new BigDecimal(Integer.valueOf(str.subSequence(30 + i, 34 + i).toString(), 16));
                                long sensor_Num1 = Integer.valueOf(str.subSequence(34 + i, 38 + i).toString(), 16);
                                String sensor_Num = String.valueOf(sensor_Num1);
                                if (sensor_Num.length() < 4) {
                                    int num = 4 - sensor_Num.length();
                                    for (int i1 = 0; i1 < num; i1++) {
                                        sensor_Num = "0" + sensor_Num;
                                    }
                                }
                                long machine_id1 = Integer.valueOf(str.subSequence(10, 14).toString(), 16);
                                String machine_id = String.valueOf(machine_id1);
                                if (machine_id.length() < 4) {
                                    int num = 4 - machine_id.length();
                                    for (int i1 = 0; i1 < num; i1++) {
                                        machine_id = "0" + machine_id;
                                    }
                                }
                                long welder_id1 = Integer.valueOf(str.subSequence(14, 18).toString(), 16);
                                String welder_id = String.valueOf(welder_id1);
                                if (welder_id.length() < 4) {
                                    int num = 4 - welder_id.length();
                                    for (int i1 = 0; i1 < num; i1++) {
                                        welder_id = "0" + welder_id;
                                    }
                                }
                                long code1 = Integer.valueOf(str.subSequence(18, 26).toString(), 16);
                                String code = String.valueOf(code1);
                                if (code.length() < 8) {
                                    int num = 8 - code.length();
                                    for (int i1 = 0; i1 < num; i1++) {
                                        code = "0" + code;
                                    }
                                }
                                long year = Integer.valueOf(str.subSequence(40 + i, 42 + i).toString(), 16);
                                String yearstr = String.valueOf(year);
                                long month = Integer.valueOf(str.subSequence(42 + i, 44 + i).toString(), 16);
                                String monthstr = String.valueOf(month);
                                long day = Integer.valueOf(str.subSequence(44 + i, 46 + i).toString(), 16);
                                String daystr = String.valueOf(day);
                                long hour = Integer.valueOf(str.subSequence(46 + i, 48 + i).toString(), 16);
                                String hourstr = String.valueOf(hour);
                                long minute = Integer.valueOf(str.subSequence(48 + i, 50 + i).toString(), 16);
                                String minutestr = String.valueOf(minute);
                                long second = Integer.valueOf(str.subSequence(50 + i, 52 + i).toString(), 16);
                                String secondstr = String.valueOf(second);
                                int status = Integer.parseInt(str.subSequence(38 + i, 40 + i).toString(), 16);

                                String timestr = yearstr + "-" + monthstr + "-" + daystr + " " + hourstr + ":" + minutestr + ":" + secondstr;
                                try {
                                    Date time = DateTools.parse("yy-MM-dd HH:mm:ss", timestr);
                                    //java.util.Date time1 = timeshow.parse(timestr);
                                    Timestamp timesql = new Timestamp(time.getTime());

                                    String fitemid = str.substring(106, 108);

                                    db.DB_Connectionmysqlrun(electricity, voltage, sensor_Num, machine_id, welder_id, code, status, fitemid, timesql, listarray1, listarray2, listarray3);
                                } catch (Exception e) {
                                    str = "";
                                    e.printStackTrace();
                                }

                            }
                            str = "";
                        } else {
                            System.out.print("数据接收校验位错误");
                            str = "";
                        }

                    } else {
                        System.out.print("数据接收长度错误");
                        str = "";
                    }

                } else {
                    //System.out.println("11");
                    System.out.print("数据接收首末位错误");
                    str = "";
                }
            }
        } catch (Exception e) {
            str = "";
            //System.out.println("S: Error 2");  
            e.printStackTrace();
        }
    }

}
