package com.cykj.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Date date;
    private static long currentTime;
    private static final long period = 900000;

    /**
     * @description: 返回当前时间往后15分钟yyyy-MM-dd HH:mm:ss格式的字符串
     * @param: 无
     * @return:  expire:返回支付宝订单过期时间
     */
    public static String getExpireTime(){
        currentTime = System.currentTimeMillis();
        date = new Date(currentTime+period);
        return simpleDateFormat.format(date);
    }


    public static long getCurrentTime(){
        currentTime = System.currentTimeMillis();
        return currentTime;
    }


}
