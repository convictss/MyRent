package com.convict;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @Author Convict
 * @Date 2019/3/16 14:45
 */
public class TestNull {

    @Test
    public void test1() {
        String s = "\n" +
                "            city_id:'12',\n" +
                "            prop_id:'1309642725',\n" +
                "            broker_id:'7025001',\n" +
                "            is_tw:0\n" +
                "        ";
        JSONObject jsonObject = JSON.parseObject(s);
        System.out.println(jsonObject.get("city_id"));

    }


}
