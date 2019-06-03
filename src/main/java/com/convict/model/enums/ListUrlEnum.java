package com.convict.model.enums;


/**
 * @Author Convict
 * @Date 2018/10/21 19:32
 */
public enum ListUrlEnum {
    AnJuKe("https://gz.zu.anjuke.com/fangyuan/p"),
    FiveEightTongCheng("http://gz.58.com/zufang/pn"),
    FangTianXia("http://gz.zu.fang.com/house/i3"),
    LeYouJia("https://guangzhou.leyoujia.com/zf/?n="),
    GanJiWang("http://gz.ganji.com/zufang/pn"),
    BaiXingWang("http://guangzhou.baixing.com/zhengzu/?page="),
    LianJia("https://gz.lianjia.com/zufang/pg"),
    QFangWang("https://guangzhou.qfang.com/rent/f"),
    ;

    private String value;

    ListUrlEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
