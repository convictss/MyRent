package com.convict.util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CommonUtil {

//    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Date类型转字符串
     * @param date
     * @param format
     * @return
     */
    public static String date2Str(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 字符串转Date类型
     * @param str
     * @param format
     * @return
     * @throws Exception
     */
    public static Date str2Date(String str, String format) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(str);
    }


    /**
     * 获取时间差
     * @param begin
     * @param end
     * @return
     */
    public static Integer getTimeSpan(Date begin, Date end) {
        long beginTime = begin.getTime();
        long endTime = end.getTime();
        if ((endTime - beginTime) / 1000 != 0) {
            return new Long((endTime - beginTime) / 1000 + 1).intValue();
        } else {
            return new Long((endTime - beginTime) / 1000).intValue();
        }
    }

    /**
     * 格式化爬虫日志
     * @param e
     * @param curUrl
     * @return
     */
    public static String formatException(Exception e, String curUrl) {
        StringBuffer sb = new StringBuffer(curUrl + "\n出现异常\n");
        // 获取异常抛出的栈结构
        StackTraceElement[] st = e.getStackTrace();
        for (StackTraceElement ste : st) {
            String exClass = ste.getClassName();
            String method = ste.getMethodName();
            sb.append("\n类：").append(exClass).append(" ")
                    .append("\n方法为：").append(method)
                    .append("\n行数为：").append(ste.getLineNumber())
                    .append("\n异常类型：").append(e.getClass().getName())
                    .append("\n-----------------\n\n");
        }
        return sb.toString();
    }

    /**
     * 获取uuid
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 根据字符串获取md5加密值
     * @param s
     * @return
     */
    public static String getMD5Value(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
