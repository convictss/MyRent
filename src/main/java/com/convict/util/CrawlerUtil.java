package com.convict.util;

import com.convict.model.DataSourceExtra;
import com.convict.model.enums.ListUrlEnum;

/**
 * @Author Convict
 * @Date 2018/12/7 14:21
 */
public class CrawlerUtil {

    /**
     * 根据dataSource返回额外信息
     * 对应Spider名字、listURL、log在session域的key、rents在session域的key、dataSource对应的表名
     *
     * @param dataSource
     * @return
     */
    public static DataSourceExtra getExtra(String dataSource) {
        switch (dataSource) {
            case "安居客":
                return new DataSourceExtra("anJuKeSpider", ListUrlEnum.AnJuKe.getValue(), "anJuKeLogPageInf", "anJuKePageInf", "anjuke", "anJuKe");
            case "58同城":
                return new DataSourceExtra("fiveEightTongChengSpider", ListUrlEnum.FiveEightTongCheng.getValue(), "fiveEightTongChengLogPageInf", "fiveEightTongChengPageInf", "58tongcheng", "fiveEightTongCheng");
            case "房天下":
                return new DataSourceExtra("fangTianXiaSpider", ListUrlEnum.FangTianXia.getValue(), "fangTianXiaLogPageInf", "fangTianXiaPageInf", "fangtianxia", "fangTianXia");
            case "乐有家":
                return new DataSourceExtra("leYouJiaSpider", ListUrlEnum.LeYouJia.getValue(), "leYouJiaLogPageInf", "leYouJiaPageInf", "leyoujia", "leYouJia");
            case "赶集网":
                return new DataSourceExtra("ganJiWangSpider", ListUrlEnum.GanJiWang.getValue(), "ganJiWangLogPageInf", "ganJiWangPageInf", "ganjiwang", "ganJiWang");
            case "百姓网":
                return new DataSourceExtra("baiXingWangSpider", ListUrlEnum.BaiXingWang.getValue(), "baiXingWangLogPageInf", "baiXingWangPageInf", "baixingwang", "baiXingWang");
            case "链家":
                return new DataSourceExtra("lianJiaSpider", ListUrlEnum.LianJia.getValue(), "lianJiaLogPageInf", "lianJiaPageInf", "lianjia", "lianJia");
            case "Q房网":
                return new DataSourceExtra("qFangWangSpider", ListUrlEnum.QFangWang.getValue(), "qFangWangLogPageInf", "qFangWangPageInf", "qfangwang", "qFangWang");
            default:
                return new DataSourceExtra();
        }
    }
}
