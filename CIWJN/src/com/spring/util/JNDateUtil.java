package com.spring.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JNDateUtil {

    private static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy_MM_dd");
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String getNowTableName() {
        String nowMonday = sdfDate.format(getNowMonday());
        String nowSunday = sdfDate.format(getNowSunday());
        //命名方式为周一到周日：rtData2021_04_05_2021_04_11
        return ("rtdata" + nowMonday + "_" + nowSunday);
    }

    // 获得下周星期一的日期
    public static Date getNextMonday() {
        int mondayPlus = getMondayPlus(new Date());
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, mondayPlus + 7);
        return currentDate.getTime();
    }

    // 获得下周星期日的日期
    public static Date getNextSunday() {
        int sundayPlus = getSundayPlus(new Date());
        GregorianCalendar currentDate = new GregorianCalendar();
        currentDate.add(GregorianCalendar.DATE, sundayPlus + 7);
        return currentDate.getTime();
    }

    // 获得当前日期与本周日相差的天数
    private static int getMondayPlus(Date gmtCreate) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(gmtCreate);
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 1; // 因为按中国礼拜一作为第一天所以这里减1
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }

    // 获得当前日期与本周日相差的天数
    private static int getSundayPlus(Date gmtCreate) {
        Calendar cd = Calendar.getInstance();
        cd.setTime(gmtCreate);
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK) - 7; // 下周日要减7
        if (dayOfWeek == 1) {
            return 0;
        } else {
            return 1 - dayOfWeek;
        }
    }

    //获取本周周一日期
    public static Date getNowMonday() {
        Calendar cld = Calendar.getInstance(Locale.CHINA);
        cld.setFirstDayOfWeek(Calendar.MONDAY);//以周一为首日
        cld.setTimeInMillis(System.currentTimeMillis());//当前时间
        cld.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);//周一
        return cld.getTime();
    }

    //获取本周周日日期
    public static Date getNowSunday() {
        Calendar cld = Calendar.getInstance(Locale.CHINA);
        cld.setFirstDayOfWeek(Calendar.MONDAY);//以周一为首日
        cld.setTimeInMillis(System.currentTimeMillis());//当前时间
        cld.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);//周日
        return cld.getTime();
    }

    /**
     * 日期格式：yyyy-MM-dd HH:mm:ss
     * @param beginTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public static List<String> getRtDataTableList(String beginTime,String endTime) throws ParseException {
        List<String> list = new ArrayList<>();
        Date beginDate = sdf.parse(beginTime);
        Date endDate = sdf.parse(endTime);
        List<Date> lDate = findDates(beginDate, endDate);
        if (lDate.size() > 0){
            for (Date date : lDate) {
                Date firstDayOfWeek = getFirstDayOfWeek(date);
                Date lastDayOfWeek = getLastDayOfWeek(date);
                String table = "rtdata" + sdfDate.format(firstDayOfWeek) + "_" + sdfDate.format(lastDayOfWeek);
                if (!list.contains(table)) {
                    list.add(table);
                }
            }
        }
        return list;
    }

    /**
     * 根据时间段，返回时间段内的数据库表
     * @param beginTime
     * @param endTime
     * @return
     */
    public static List<String> getRtDataTableList(Date beginTime, Date endTime) {
        List<String> list = new ArrayList<>();
        List<Date> lDate = findDates(beginTime, endTime);
        if (lDate.size() > 0){
            for (Date date : lDate) {
                Date firstDayOfWeek = getFirstDayOfWeek(date);
                Date lastDayOfWeek = getLastDayOfWeek(date);
                String table = "rtdata" + sdfDate.format(firstDayOfWeek) + "_" + sdfDate.format(lastDayOfWeek);
                if (!list.contains(table)) {
                    list.add(table);
                }
            }
        }
        return list;
    }

    /**
     * 获取时间段内所有的日期
     *
     * @param dBegin
     * @param dEnd
     * @return
     */
    public static List<Date> findDates(Date dBegin, Date dEnd) {
        List<Date> lDate = new ArrayList<>();
        lDate.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        return lDate;
    }


    /**
     * 获取指定日期所在周的周一
     *
     * @param date 日期
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            c.add(Calendar.DAY_OF_MONTH, -1);
        }
        c.add(Calendar.DATE, c.getFirstDayOfWeek() - c.get(Calendar.DAY_OF_WEEK) + 1);
        return c.getTime();
    }

    /**
     * 获取指定日期所在周的周日
     *
     * @param date 日期
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // 如果是周日直接返回
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            return date;
        }
        c.add(Calendar.DATE, 7 - c.get(Calendar.DAY_OF_WEEK) + 1);
        return c.getTime();
    }

}
