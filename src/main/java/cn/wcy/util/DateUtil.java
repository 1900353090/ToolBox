package cn.wcy.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.processing.Completion;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>Title : DateUtil.java</p>
 * <p>Description : 时间操作工具类</p>
 * <p>DevelopTools : IntelliJ IDEA 2018.2.3 x64</p>
 * <p>DevelopSystem : Windows 10</p>
 * <p>Company : org.wcy</p>
 * @author : WangChenYang
 * @date : 2019/8/31 17:01
 * @version : 0.0.1
 */
public class DateUtil {

    public enum DateMoveType {
        YEAR(1,"年"),
        MONTH(2,"月"),
        DAY(5,"日"),
        HOUR(11,"小时"),
        MINUTE(12,"分钟"),
        SECOND(13,"秒"),
        MILLISECOND(14,"毫秒");
        private Integer value;
        private String desc;

        DateMoveType(Integer value,String desc){
            this.value = value;
            this.desc = desc;
        }
        public Integer getValue(){
            return this.value;
        }
    }

    /**
    * @Description: 字符串转日期
    * @Param: 日期date
    * @return: date类型日期
    * @Author: 王晨阳
    * @Date: 2019/12/7-13:58
    */
    public static Date stringToDate(String date, DateUtil.Format format) throws Exception {
        DateFormat dateFormat = new SimpleDateFormat(format.getValue());
        return dateFormat.parse(date);
    }

    public enum Format {
        YYYY("yyyy","年"),
        YYYY_MM("yyyy-MM","月"),
        YYYY_MM_DD("yyyy-MM-dd","日"),
        YYYY_MM_DD_HH("yyyy-MM-dd HH","时"),
        YYYY_MM_DD_HH_MM("yyyy-MM-dd HH:mm","分"),
        YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss","秒");
        private String value;
        private String desc;
        Format(String value,String desc){
            this.value = value;
            this.desc = desc;
        }
        public String getValue(){
            return this.value;
        }
    }

    /**
     * 传入需要推移的天数获取日期
     * @param date 需要推移的日期, setTime 推移的天数, type 需要推移的类型, format 日期格式的类型
     * @return: 日期
     * @Author: 王晨阳
     * @Date: 2019/8/19-11:29
     */
    public static String moveTime(Date date, int setTime, DateUtil.DateMoveType type, DateUtil.Format format) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(type.getValue(),setTime);//把日期往前减少一天，若想把日期向后推一天则将负数改为正数
        date=calendar.getTime();
        SimpleDateFormat formatter1 = new SimpleDateFormat(format.getValue());
        return formatter1.format(date);
    }

    /**
     * @Description: localDateTime转string
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/3/17-11:48
    */
    public static String getString(LocalDateTime localDateTime, DateUtil.Format format) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format.getValue());
        return localDateTime.format(dtf);
    }

    /**
     * @Description: 获取两个月之间的差
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/3/25-20:47
    */
    public static Integer getMonthSubstrut(String startTime, String endTime) throws Exception {
        String[] split = endTime.split("-");
        String[] split1 = startTime.split("-");
        if(split.length < 2 && split1.length < 2) {
            throw new Exception("Date format error, The lack of month");
        }
        try {
            int year = Integer.valueOf(split[0]);
            int month = Integer.valueOf(split[1]);
            int year1 = Integer.valueOf(split1[0]);
            int month1 = Integer.valueOf(split1[1]);
            int y = year-year1;
            return (y*12+month)-month1;
        }catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Date format error -> "+e.getMessage());
        }
    }

    /**
     * @Description: 获取当前时间
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/2/20-15:34
     */
    public static String getNowTime(DateUtil.Format format) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat(format.getValue());
        return formatter.format(calendar.getTime());
    }

    /**
     * @Description: 获取两个日期相差天数
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/3/25-20:45
    */
    public static long getDaySubstrut(String startDate, String endDate, DateUtil.Format format) throws Exception {
        //设置转换的日期格式
        SimpleDateFormat sdf = new SimpleDateFormat(format.getValue());
        //开始时间
        Date sDate = sdf.parse(startDate);
        //结束时间
        Date eDate = sdf.parse(endDate);
        //得到相差的天数 betweenDate
        return (sDate.getTime() - eDate.getTime())/(60*60*24*1000);
    }

    /**
     * @Description: 获取当天剩余秒数
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/2/20-15:30
     */
    public static long getSeconds(){
        Calendar curDate = Calendar.getInstance();
        Calendar tommorowDate = new GregorianCalendar(curDate
                .get(Calendar.YEAR), curDate.get(Calendar.MONTH), curDate
                .get(Calendar.DATE) + 1, 0, 0, 0);
        return (tommorowDate.getTimeInMillis() - curDate .getTimeInMillis()) / 1000;
    }

    //private static int jisuan(int a) {
    //    a++;
    //    return a;
    //}
    //
    //private static int a = 0;
    //
    //private static final String lock = "lock";
    //public static void main(String[] args) {
    //    System.out.println(a);
    //    //开启三条线程同时操作静态变量a
    //    for(int i=0;i<3;i++) {
    //        new Thread(new Runnable() {
    //            @Override
    //            public void run() {
    //                while (true) {
    //                    //同步锁获取lock，未获取到的线程等在在这等着获取
    //                    synchronized (lock){
    //                        //获取到锁的线程判断a变量是否超出范围，此时的a已经是其他线程操作后的值了
    //                        if(a<20) {
    //                            //当其中一个线程调用jisuan函数时传入的则是其他线程操作后的值
    //                            a = DateUtil.jisuan(a);
    //                            //打印jisuan函数执行后的值
    //                            System.out.println(a);
    //                        }else {
    //                            return;
    //                        }
    //                    }
    //                }
    //            }
    //        }).start();
    //    }
    //}

    /**
     * @Description: 获取上个月天数
     * @Author: 王晨阳
     * @Date: 2019/10/21-13:30
     */
    public static int getLastDay() {
        //取得系统当前时间
        Calendar cal = Calendar.getInstance();
        //取得系统当前时间所在月第一天时间对象
        cal.set(Calendar.DAY_OF_MONTH, 1);
        //日期减一,取得上月最后一天时间对象
        cal.add(Calendar.DAY_OF_MONTH, -1);
        //输出上月最后一天日期
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @Description: 获取某月的最后一天日期
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/5/29-16:57
     */
    public static String getLastDayOfMonth(int year,int month) {
        Calendar calendar = Calendar.getInstance();
        // 设置时间,当前时间不用设置
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month-1);

        // System.out.println(calendar.getTime());

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(calendar.getTime());
    }

    /**
     * @Description: 获取当月的 天数
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/5/29-17:24
    */
    public static int getCurrentMonthDay() {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        return a.get(Calendar.DATE);
    }

    /**
     * @Description: 根据年 月 获取对应的月份 天数
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/5/29-17:24
    */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        return a.get(Calendar.DATE);
    }

    /**
     * @Description: 时间选择
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/5/29-16:31
    */
    public enum Timing {
        BEGIN(1,"一个日期的开始时间"),
        END(2,"一个日期的结束时间");

        private Integer value;
        private String desc;

        Timing(Integer value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public Integer getValue() {
            return this.value;
        }

        public String getDesc() {
            return this.desc;
        }
    }

    private static Map<String,String> loadMap(String date) {
        Map<String,String> map = new HashMap<>();
        map.put(Timing.BEGIN.name()+"-"+1,"-01-01 00:00:00");
        int daysByYearMonth = getDaysByYearMonth(Integer.valueOf(date.split("-")[0]), 12);
        String day = daysByYearMonth+"";
        if(daysByYearMonth < 10) {
            day = "0"+daysByYearMonth;
        }
        map.put(Timing.END.name()+"-"+1,"-12-"+day+" 23:59:59");
        if(date.length() <= 4) {
            return map;
        }
        daysByYearMonth = getDaysByYearMonth(Integer.valueOf(date.split("-")[0]), Integer.valueOf(date.split("-")[1]));
        day = daysByYearMonth+"";
        if(daysByYearMonth < 10) {
            day = "0"+daysByYearMonth;
        }
        map.put(Timing.BEGIN.name()+"-"+2,"-01 00:00:00");
        map.put(Timing.BEGIN.name()+"-"+3," 00:00:00");
        map.put(Timing.END.name()+"-"+2,"-"+day+" 23:59:59");
        map.put(Timing.END.name()+"-"+3," 23:59:59");

        map.put(Timing.BEGIN.name()+":"+1,":00:00");
        map.put(Timing.BEGIN.name()+":"+2,":00");
        map.put(Timing.BEGIN.name()+":"+3,".000");

        map.put(Timing.END.name()+":"+1,":59:59");
        map.put(Timing.END.name()+":"+2,":59");
        map.put(Timing.END.name()+":"+3,".999");
        return map;
    }

    /**
     * @Description: 补齐时间
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/5/29-16:28
    */
    public static String completionTime(String date, DateUtil.Timing timing) throws RuntimeException {
        if(StringUtils.isEmpty(date)) {
            throw new RuntimeException("the date cannot be empty");
        }
        if(Objects.isNull(timing)) {
            throw new RuntimeException("the timing type cannot be empty");
        }
        Map<String, String> map = loadMap(date);
        if(date.split(" ").length == 2) {
            String[] type = date.split(":");
            return date+map.get(timing.name()+":"+type.length);
        }else {
            String[] type = date.split("-");
            return date+map.get(timing.name()+"-"+type.length);
        }
    }

    /**
     * @Description: 字符串日期转localDateTime
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/12-13:55
     */
    public static LocalDateTime stringToLocalDateTime(String time) {
        if(StringUtils.isEmpty(time)) {
            return null;
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(time,df);
    }

    /**
     * @Description: 计算两个时间点的天数差
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/12-14:00
     */
    public static int dateDiff(LocalDateTime dt1,LocalDateTime dt2){
        //获取第一个时间点的时间戳对应的秒数
        long t1 = dt1.toEpochSecond(ZoneOffset.ofHours(0));
        //获取第一个时间点在是1970年1月1日后的第几天
        long day1 = t1 /(60*60*24);
        //获取第二个时间点的时间戳对应的秒数
        long t2 = dt2.toEpochSecond(ZoneOffset.ofHours(0));
        //获取第二个时间点在是1970年1月1日后的第几天
        long day2 = t2/(60*60*24);
        //返回两个时间点的天数差
        return (int)(day2 - day1);
    }

    /**
     * @Description: 获取两个时间点的月份差
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/12-13:59
     */
    public static int monthDiff(LocalDateTime dt1,LocalDateTime dt2){
        //获取第一个时间点的月份
        int month1 = dt1.getMonthValue();
        //获取第一个时间点的年份
        int year1 = dt1.getYear();
        //获取第一个时间点的月份
        int month2 = dt2.getMonthValue();
        //获取第一个时间点的年份
        int year2 = dt2.getYear();
        //返回两个时间点的月数差
        return (year2 - year1) *12 + (month2 - month1);
    }

    /**
     * @Description: 每次递归选出最小值并排除
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/5/30-11:27
    */
    public static String dg(String str) {
        if(StringUtils.isEmpty(str)) {
            return "";
        }
        String[] split = str.split("");
        int min = Integer.valueOf(split[0]);
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<split.length;i++) {
            if(i < split.length-1) {
                if(min > Integer.valueOf(split[i+1])) {
                    sb.append(min);
                    min = Integer.valueOf(split[i+1]);
                }else {
                    sb.append(Integer.valueOf(split[i+1]));
                }
            }
        }
        String dg = dg(sb.toString());
        return min+dg;
    }

    /**
     * @Description: 时间选择
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/5/29-16:31
     */
    public enum DateRank {
        DESC(1,"倒序"),
        ASC(2,"正序");

        private Integer value;
        private String desc;

        DateRank(Integer value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public Integer getValue() {
            return this.value;
        }

        public String getDesc() {
            return this.desc;
        }
    }

    /**
     * @Description: 字符串日期排序
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/6/20-15:19
     */
    public static List<String> sort(List<String> times,DateRank dateRank) {
        if(CollectionUtils.isEmpty(times)) {
            return new ArrayList<>();
        }
        try {
            times.parallelStream().forEach(obj -> obj = completionTime(obj, Timing.BEGIN));
        }catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("error");
        }
        List<String> keys = new ArrayList<>(times);
        String k = keys.get(0);
        String[] length = keys.get(0).split("&")[0].split("-");
        int a = length.length>2?2:1;
        String[] time = keys.get(0).split("&")[0].split("-");
        String str;
        if(a==2) {
            String[] dhms = time[2].split(" ");
            String[] hms = dhms[1].split(":");
            str = time[0]+time[1]+dhms[0]+hms[0]+hms[1]+hms[2];
        }else {
            str = time[0]+time[1];
        }
        long old = Long.parseLong(str);
        List<String> ks = new LinkedList<>();
        for(int i=0;i<keys.size();i++) {
            if(i < keys.size()-1) {
                String[] nowTime = keys.get(i+1).split("&")[0].split("-");
                long now;
                if(a==2) {
                    String[] dhms = nowTime[2].split(" ");
                    String[] hms = dhms[1].split(":");
                    now = Long.parseLong(nowTime[0]+nowTime[1]+dhms[0]+hms[0]+hms[1]+hms[2]);
                }else {
                    now = Long.parseLong(nowTime[0] + nowTime[1]);
                }
                boolean judge = DateRank.DESC.equals(dateRank)?old < now:old > now;
                if(judge) {
                    ks.add(k);
                    old = now;
                    k = keys.get(i+1);
                }else {
                    ks.add(keys.get(i+1));
                }
            }
        }
        List<String> sort = sort(ks,dateRank);
        List<String> allkeys = new LinkedList<>();
        allkeys.add(k);
        allkeys.addAll(sort);
        return allkeys;
    }

    /**
     * @Description: 类型转换
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/7/13-13:33
    */
    public static Date valueOf(String time, DateUtil.Format format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format.getValue());//注意月份是MM
        try {
            return simpleDateFormat.parse(time);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @Description: 类型转换
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/7/13-13:33
     */
    public static Date valueOf(LocalDateTime time) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = time.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * @Description: 类型转换
     * @Author: 王晨阳
     * @LastUpdater: 王晨阳
     * @Date: 2020/7/13-14:01
    */
    public static String valueOf(Date time, DateUtil.Format format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format.getValue());
        String dateString = formatter.format(time);
        return dateString;
    }

    /**
     * @Description: 时间相减
     * @Param: startTime开始时间，endTime结束时间，type结果类型，paraFormat入参格式
     * @LastUpdater: 王晨阳
     * @Date: 2020/7/13-13:21
    */
    public static Long subtract(Date startTime, Date endTime, DateUtil.Format paraFormat) {
        String s = DateUtil.completionTime(valueOf(startTime,paraFormat), Timing.BEGIN);
        String e = DateUtil.completionTime(valueOf(endTime,paraFormat), Timing.END);
        Date sd = valueOf(s, paraFormat);
        Date ed = valueOf(e, paraFormat);
        if(Objects.nonNull(sd) && Objects.nonNull(ed)) {
            long sl = sd.getTime();
            long el = ed.getTime();
            return el-sl;
        }
        return 0L;
    }

    public static void main(String[] args) {
        //String str = "986371";
        //String dg = dg(str);
        //System.out.println(dg);
        //int a = 10;
        //int meter = 100;
        //int allMeter = 0;
        //int lastMeter = 0;
        //for(int i=0;i<a;i++) {
        //    allMeter += meter;
        //    meter = meter/2;
        //    if(i == a-1) {
        //        lastMeter = meter;
        //    }
        //}
        //System.out.println("总高度："+allMeter);
        //System.out.println("最后一次高度："+lastMeter);
        //List<String> times = new LinkedList<>();
        //times.add("2020-05-12 00:12:12");
        //times.add("2020-06-12 12:12:12");
        //times.add("2020-06-12 20:12:12");
        //times.add("2020-04-19 10:12:12");
        //List<String> sort = sort(times,DateRank.DESC);
        //for(String time : sort) {
        //    System.out.println(time);
        //}
        Long subtract = subtract(valueOf("2020-05-12 00:12:12", Format.YYYY_MM_DD_HH_MM_SS), valueOf("2020-05-12 00:13:12", Format.YYYY_MM_DD_HH_MM_SS), Format.YYYY_MM_DD_HH_MM_SS);
        System.out.println(subtract);
    }

    /**
     * 时间转时间戳
     * @author 王晨阳
     * @lastUpdaterAuthor 王晨阳
     * @date 2021/1/20-15:49
     * @version 0.0.1
     */
    public long dateToStamp(String dateTime) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(dateTime);
        return date.getTime();
    }

    /**
     * @Description: 获取本月天数
     * @Author: 王晨阳
     * @Date: 2019/10/21-13:30
     */
    public static int getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取本周的周一日期和周日日期
     * @author 王晨阳
     * @lastUpdaterAuthor 王晨阳
     * @date 2021/1/18-13:59
     * @version 0.0.1
     */
    public static String[] getWeekDate(String dateTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); //设置时间格式
            Calendar cal = Calendar.getInstance();
            Date time = sdf.parse(dateTime);
            cal.setTime(time);
            System.out.println("要计算日期为:"+sdf.format(cal.getTime())); //输出要计算日期
            //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
            int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
            if(1 == dayWeek) {
                cal.add(Calendar.DAY_OF_MONTH, -1);
            }
            cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
            int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
            cal.add(Calendar.DATE, cal.getFirstDayOfWeek()-day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
            System.out.println("所在周星期一的日期："+sdf.format(cal.getTime()));
            cal.add(Calendar.DATE, 6);
            System.out.println("所在周星期日的日期："+sdf.format(cal.getTime()));
            return new String[]{sdf.format(cal.getTime()), sdf.format(cal.getTime())};
        } catch (ParseException e) {
            e.printStackTrace();
            return new String[]{"",""};
        }
    }
}
