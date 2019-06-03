package com.convict.util.site;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnjukeUtil {

    // 获取房子类型
    public static String getHouseTypeByStr (String origin) {
        Pattern pattern = Pattern.compile("\\d+室\\d+厅");
        Matcher matcher = pattern.matcher(origin.trim());
        String houseType = "";
        if (matcher.find()) {
            houseType = matcher.group(0);
        }
        return houseType;
    }

    // 获取面积
    public static String getAreaByStr (String origin) {
        Pattern pattern = Pattern.compile(".*[\\u4e00-\\u9fa5]+(\\d+)平米.*");
        Matcher matcher = pattern.matcher(origin);
        String area = "";
        if (matcher.find()) {
            area = matcher.group(1);
        }
        return area;
    }

    // 获取楼层
    public static String getFloorByStr (String origin) {
        Pattern pattern = Pattern.compile("平米(.*层\\))");
        Matcher matcher = pattern.matcher(origin);
        String floor = "";
        if (matcher.find()) {
            floor = matcher.group(1);
        }
        return floor;
    }

    // 获取区域
    public static String getRegionByStr (String origin) {
        Pattern pattern = Pattern.compile("([\\u4e00-\\u9fa5]+)-");
        Matcher matcher = pattern.matcher(origin);
        String region = "";
        if (matcher.find()) {
            region = matcher.group(1);
        }
        return region;
    }

    // 获取具体地址
    public static String getAddressByStr (String origin) {
        Pattern pattern = Pattern.compile("-(.*)");
        Matcher matcher = pattern.matcher(origin);
        String address = "";
        if (matcher.find()) {
            address = matcher.group(1);
        }
        return address;
    }

}
